package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.geo.Feature;
import com.alibaba.fastjson.support.geo.FeatureCollection;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.configurations.FeatureName;
import me.Votifier.server.model.configurations.bins.Bin;
import me.Votifier.server.model.configurations.bins.BinsConfig;
import me.Votifier.server.model.documents.HeatmapDocuments.EconomicHeatMap;
import me.Votifier.server.model.documents.HeatmapDocuments.RacialHeatMap;
import me.Votifier.server.model.RacialGroup;
import me.Votifier.server.model.documents.HeatmapDocuments.RegionTypeHeatMap;
import me.Votifier.server.model.documents.HeatmapDocuments.ElectionHeatMap;

import me.Votifier.server.model.exceptions.InvalidBinRangeException;
import me.Votifier.server.model.exceptions.InvalidRacialGroupException;

@Service
public class MapService {

    private static Map<FeatureName, HashMap<Integer, Bin>> loadedBins = new HashMap<>();
    private static Map<String, Integer> economicBinStringToIndexMapping = new HashMap<>();
    private static Map<Integer, String> indexToEconomicBinStringMapping = new HashMap<>();
    private static final int INVALID_MAPPING_INDEX = -1;
    private static final int INFINITE_BIN_RANGE_INDEX = -1;

    @Autowired
    public MapService(BinsConfig binsConfig) {
        loadedBins.putAll(binsConfig.getAllBins());
        Map<Integer, Bin> economicIncomeBins = loadedBins.get(FeatureName.HEATMAP_ECONOMIC_INCOME);
        int currentIndex = 0;
        for(Bin bin : economicIncomeBins.values()){
            economicBinStringToIndexMapping.put(bin.getBinDataLabel(), currentIndex);
            indexToEconomicBinStringMapping.put(currentIndex, bin.getBinDataLabel());
            currentIndex++;
        }
    }

    public ResponseEntity<Resource> colorHeatmapDemographic(
        RacialHeatMap racialHeatMap,
        RacialGroup selectedRacialGroup) {

    try {
        // Validate the selected racial group
        String racialGroupIdentifier = getFieldForRacialGroup(selectedRacialGroup);
        if (racialGroupIdentifier == null) {
            throw new InvalidRacialGroupException();
        }

        Map<Integer, Bin> heatmapBins = loadedBins.get(FeatureName.HEATMAP_DEMOGRAPHIC);
        final int BIN_COLOR_INDEX = 0;

        Map<String, String> precinctColorMapping = new HashMap<>();

        List<RacialHeatMap.RacialHeatMapData> data = racialHeatMap.getData();
        for (RacialHeatMap.RacialHeatMapData entry : data) {
            String uniqueId = entry.getUNIQUE_ID();
            double totalPopulation = parseDouble(entry.getTOTAL_POPULATION());
            double racialGroupPopulation = parseDouble(getRacialGroupPopulation(entry, racialGroupIdentifier));

            Bin assignedBin = assignBin(racialGroupPopulation, totalPopulation, heatmapBins);
            String colorHex = assignedBin.getBinColors()[BIN_COLOR_INDEX];
            precinctColorMapping.put(uniqueId, colorHex);
        }

        // Convert the mapping to JSON
        String jsonMapping = JSON.toJSONString(precinctColorMapping, SerializerFeature.PrettyFormat);
        Resource resource = new ByteArrayResource(jsonMapping.getBytes());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(resource);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

private String getFieldForRacialGroup(RacialGroup racialGroup) {
    switch (racialGroup) {
        case WHITE: return "WHT_NHSP22";
        case BLACK: return "BLK_NHSP22";
        case ASIAN: return "ASN_NHSP22";
        case HISPANIC: return "HSP_NHSP22";
        default: return null;
    }
}

private double parseDouble(String value) {
    try {
        return Double.parseDouble(value);
    } catch (NumberFormatException e) {
        return 0.0;
    }
}

private Bin assignBin(double groupPopulation, double totalPopulation, Map<Integer, Bin> heatmapBins) 
        throws InvalidBinRangeException {
    if (totalPopulation <= 0) {
        return heatmapBins.get(1);
    }
    int percentage = (int) ((groupPopulation / totalPopulation) * 100);

    for (Bin bin : heatmapBins.values()) {
        if (bin.isInBinRange(percentage)) {
            return bin;
        }
    }
    throw new InvalidBinRangeException();
}


private String getRacialGroupPopulation(RacialHeatMap.RacialHeatMapData entry, String racialGroupIdentifier) {
    switch (racialGroupIdentifier) {
        case "WHT_NHSP22": return entry.getWHITE();
        case "BLK_NHSP22": return entry.getBLACK();
        case "ASN_NHSP22": return entry.getASIAN();
        case "HSP_NHSP22": return entry.getHISPANIC();
        default: return "0";
    }
}


public ResponseEntity<Resource> colorHeatmapEconomicIncome(EconomicHeatMap economicHeatMap) {
    try {
        Map<Integer, Bin> loadedHeatmapBins = loadedBins.get(FeatureName.HEATMAP_ECONOMIC_INCOME);
        final int BIN_COLOR_INDEX = 0;

        Map<String, String> uniqueIdToColorMap = new HashMap<>();
        for (EconomicHeatMap.EconomicHeatMapData precinct : economicHeatMap.getData()) {
            String uniquePrecinctId = precinct.getUNIQUE_ID();
            double totalAccumulatedIncomeSum = 0.0;
            double totalHouseholds = parseDouble(precinct.getTOTAL_HOUSEHOLDS());

            if (totalHouseholds > 0.0) {
                totalAccumulatedIncomeSum += calculateBracketIncome(precinct, loadedHeatmapBins);

                int estimatedAverageHouseholdIncome = (int) (totalAccumulatedIncomeSum / totalHouseholds);

                Bin assignedBin = assignBin(estimatedAverageHouseholdIncome, loadedHeatmapBins);

                uniqueIdToColorMap.put(uniquePrecinctId, assignedBin.getBinColors()[BIN_COLOR_INDEX]);
            } else {
                Bin defaultBin = loadedHeatmapBins.get(1);
                uniqueIdToColorMap.put(uniquePrecinctId, defaultBin.getBinColors()[BIN_COLOR_INDEX]);
            }
        }

        // Convert result to JSON and return
        String stringJson = JSON.toJSONString(uniqueIdToColorMap, SerializerFeature.PrettyFormat);
        Resource resource = new ByteArrayResource(stringJson.getBytes());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(resource);

    } catch (Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

private double calculateBracketIncome(EconomicHeatMap.EconomicHeatMapData precinct, Map<Integer, Bin> loadedHeatmapBins) {
    double totalIncome = 0.0;

    Map<String, String> bracketMapping = new HashMap<>();
    if (precinct.get_0_35K() != null) bracketMapping.put("0_35K", precinct.get_0_35K());
    if (precinct.get_35_60K() != null) bracketMapping.put("35_60K", precinct.get_35_60K());
    if (precinct.get_60_100K() != null) bracketMapping.put("60_100K", precinct.get_60_100K());
    if (precinct.get_100K_125K() != null) bracketMapping.put("100K_125K", precinct.get_100K_125K());
    if (precinct.get_125K_150K() != null) bracketMapping.put("125K_150K", precinct.get_125K_150K());
    if (precinct.get_150K_MORE() != null) bracketMapping.put("150K_MORE", precinct.get_150K_MORE());

    for (String bracketKey : bracketMapping.keySet()) {
        int incomeBracketIndex = economicBinStringToIndexMapping.getOrDefault(bracketKey, -1);
        if (incomeBracketIndex != -1) {
            Bin bin = loadedHeatmapBins.get(incomeBracketIndex);
            double midpoint = calculateMidpoint(bin);
            double households = parseDouble(bracketMapping.get(bracketKey));
            totalIncome += midpoint * households;
        }
    }
    return totalIncome;
}

private double calculateMidpoint(Bin bin) {
    int[] range = bin.getBinRange();
    if (range[1] == INFINITE_BIN_RANGE_INDEX) {
        return range[0];
    }
    return (range[0] + range[1]) / 2.0;
}

private Bin assignBin(int estimatedIncome, Map<Integer, Bin> loadedHeatmapBins) throws InvalidBinRangeException {
    for (Bin bin : loadedHeatmapBins.values()) {
        if (bin.isInBinRange(estimatedIncome)) {
            return bin;
        }
    }
    throw new InvalidBinRangeException("No bin found for income: " + estimatedIncome);
}



    public ResponseEntity<Resource> colorHeatmapRegions(RegionTypeHeatMap regionTypeHeatMap) {
        try {
            Map<String, String> regionTypeToColor = Map.of(
                "urban", "#2a3eb3",
                "suburban", "#008f9d",
                "rural", "#80d043"
            );

            Map<String, String> uniqueIdToColorMap = new HashMap<>();

            for (RegionTypeHeatMap.RegionTypeHeatMapData regionData : regionTypeHeatMap.getData()) {
                String uniqueId = regionData.getUNIQUE_ID();
                String regionType = regionData.getRegion_type();
                String colorHex = regionTypeToColor.getOrDefault(regionType.toLowerCase(), "#000000");
                uniqueIdToColorMap.put(uniqueId, colorHex);
            }

            String jsonResponse = JSON.toJSONString(uniqueIdToColorMap, SerializerFeature.PrettyFormat);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);

        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Resource> colorHeatmapEconomicPoverty(EconomicHeatMap economicHeatMap) {
        try {
            Map<String, String> uniqueIdToColorMap = new HashMap<>();
    
            for (EconomicHeatMap.EconomicHeatMapData precinct : economicHeatMap.getData()) {
                String uniqueId = precinct.getUNIQUE_ID();
                int totalHouseholds = (int) parseDouble(precinct.getTOTAL_HOUSEHOLDS());
                int povertyHouseholds = (int) parseDouble(precinct.get_0_35K());

                // print totalHouseholds until count == 10
                // int count = 0;
                // if (count < 10) {
                //     System.out.println("Total Households: " + totalHouseholds);
                //     System.out.println("Poverty Households: " + povertyHouseholds);
                //     count++;
                // }

                double povertyPercentage = (totalHouseholds > 0) 
                    ? ((double) povertyHouseholds / totalHouseholds) * 100 
                    : 0.0;
    
                String colorHex = assignPovertyColor(povertyPercentage);
                uniqueIdToColorMap.put(uniqueId, colorHex);
            }
    
            // Convert result to JSON
            String jsonResponse = JSON.toJSONString(uniqueIdToColorMap, SerializerFeature.PrettyFormat);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
    
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private String assignPovertyColor(double povertyPercentage) {
        if (povertyPercentage <= 10) {
            return "#FFCCBC"; // Very Light Deep Orange (Deep Orange 100)
        } else if (povertyPercentage <= 20) {
            return "#FFAB91"; // Light Deep Orange (Deep Orange 200)
        } else if (povertyPercentage <= 30) {
            return "#FF8A65"; // Soft Deep Orange (Deep Orange 300)
        } else if (povertyPercentage <= 40) {
            return "#FF7043"; // Moderate Deep Orange (Deep Orange 400)
        } else if (povertyPercentage <= 50) {
            return "#FF5722"; // Standard Deep Orange (Deep Orange 500)
        } else if (povertyPercentage <= 60) {
            return "#F4511E"; // Richer Deep Orange (Deep Orange 600)
        } else if (povertyPercentage <= 70) {
            return "#E64A19"; // Dark Deep Orange (Deep Orange 700)
        } else {
            return "#D84315"; // Deeper, Darker Deep Orange (Deep Orange 800)
        }
    }
    
    

    public ResponseEntity<Resource> colorHeatmapEconomicPolitical(EconomicHeatMap economicHeatMap, ElectionHeatMap electionHeatMap) {
        try {
            Map<Integer, Bin> loadedHeatmapBins = loadedBins.get(FeatureName.HEATMAP_ECONOMIC_INCOME);
            final int BIN_COLOR_INDEX = 0;
    
            // Map UNIQUE_ID -> Political LEAN
            Map<String, String> precinctLeanMap = new HashMap<>();
            for (ElectionHeatMap.ElectionHeatMapData electionData : electionHeatMap.getData()) {
                precinctLeanMap.put(electionData.getUNIQUE_ID(), electionData.getLEAN());
            }
    
            // Map UNIQUE_ID -> Color
            Map<String, String> uniqueIdToColorMap = new HashMap<>();
            for (EconomicHeatMap.EconomicHeatMapData precinct : economicHeatMap.getData()) {
                String uniquePrecinctId = precinct.getUNIQUE_ID();
                double totalAccumulatedIncomeSum = 0.0;
                double totalHouseholds = parseDouble(precinct.getTOTAL_HOUSEHOLDS());
    
                if (totalHouseholds > 0.0) {
                    totalAccumulatedIncomeSum += calculateBracketIncome(precinct, loadedHeatmapBins);
    
                    int estimatedAverageHouseholdIncome = (int) (totalAccumulatedIncomeSum / totalHouseholds);
                    Bin assignedBin = assignBin(estimatedAverageHouseholdIncome, loadedHeatmapBins);
    
                    // Determine political lean and assign color
                    String lean = precinctLeanMap.getOrDefault(uniquePrecinctId, "Neutral");
                    String colorHex = assignPoliticalColor(assignedBin.getBinNumber(), lean);
                    uniqueIdToColorMap.put(uniquePrecinctId, colorHex);
                } else {
                    Bin defaultBin = loadedHeatmapBins.get(1);
                    String lean = precinctLeanMap.getOrDefault(uniquePrecinctId, "Neutral");
                    String colorHex = assignPoliticalColor(defaultBin.getBinNumber(), lean);
                    uniqueIdToColorMap.put(uniquePrecinctId, colorHex);
                }
            }
    
            // Convert result to JSON and return
            String stringJson = JSON.toJSONString(uniqueIdToColorMap, SerializerFeature.PrettyFormat);
            Resource resource = new ByteArrayResource(stringJson.getBytes());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
    
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private String assignPoliticalColor(int binNumber, String lean) {
        switch (binNumber) {
            case 1: return lean.equals("Republican") ? "#FFCDD2" : "#BBDEFB"; // Light Red / Light Blue
            case 2: return lean.equals("Republican") ? "#EF9A9A" : "#90CAF9"; // Red / Blue
            case 3: return lean.equals("Republican") ? "#E57373" : "#64B5F6"; // Medium Red / Medium Blue
            case 4: return lean.equals("Republican") ? "#EF5350" : "#42A5F5"; // Deep Red / Deep Blue
            case 5: return lean.equals("Republican") ? "#F44336" : "#2196F3"; // Bright Red / Bright Blue
            case 6: return lean.equals("Republican") ? "#D32F2F" : "#1976D2"; // Dark Red / Dark Blue
            default: return "#BDBDBD"; // Neutral Grey
        }
    }
    

    public String constructJSONString(BufferedReader responseBodyReader, StringBuilder jsonContentBuilder) throws IOException {
        String currentJsonLine = "";
        while ((currentJsonLine = responseBodyReader.readLine()) != null) {
            jsonContentBuilder.append(currentJsonLine);
        }
        responseBodyReader.close();
        return jsonContentBuilder.toString();
    }
}