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

import me.Votifier.server.model.exceptions.InvalidBinRangeException;
import me.Votifier.server.model.exceptions.InvalidRacialGroupException;

@Service
public class MapService {

    private static Map<FeatureName, HashMap<Integer, Bin>> loadedBins = new HashMap<>();
    private static Map<String, Integer> economicBinStringToIndexMapping = new HashMap<>();
    private static Map<Integer, String> indexToEconomicBinStringMapping = new HashMap<>();
    private static final int INVALID_MAPPING_INDEX = -1;
    private static final int INFINITE_BIN_RANGE_INDEX = -1;
    private static final String TOTAL_HOUSEHOLDS_IDENTIFIER = "TOT_HOUS22";

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

        // Load heatmap bins
        Map<Integer, Bin> heatmapBins = loadedBins.get(FeatureName.HEATMAP_DEMOGRAPHIC);
        final int BIN_COLOR_INDEX = 0;

        // Prepare the result map for UNIQUE_ID -> ColorHex
        Map<String, String> precinctColorMapping = new HashMap<>();

        // Iterate over RacialHeatMapData entries
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
        e.printStackTrace(); // Log exception for debugging
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

        // Process each precinct's economic data
        for (EconomicHeatMap.EconomicHeatMapData precinct : economicHeatMap.getData()) {
            String uniquePrecinctId = precinct.getUNIQUE_ID();
            double totalAccumulatedIncomeSum = 0.0;
            double totalHouseholds = parseDouble(precinct.getTOTAL_HOUSEHOLDS());

            if (totalHouseholds > 0.0) {
                // Calculate total income based on income brackets
                totalAccumulatedIncomeSum += calculateBracketIncome(precinct, loadedHeatmapBins);

                // Compute estimated average household income
                int estimatedAverageHouseholdIncome = (int) (totalAccumulatedIncomeSum / totalHouseholds);

                // Assign bin based on average income
                Bin assignedBin = assignBin(estimatedAverageHouseholdIncome, loadedHeatmapBins);

                // Store the color
                uniqueIdToColorMap.put(uniquePrecinctId, assignedBin.getBinColors()[BIN_COLOR_INDEX]);
            } else {
                // Default bin for invalid data
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



    public ResponseEntity<Resource> colorHeatmapEconomicRegions(StateAbbreviation stateAbbreviation) {
        // TO DO: (GUI-5) Color the precinct heatmap appropriately based on the threshold-defined region type in each precinct
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapEconomicPoverty(StateAbbreviation stateAbbreviation) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapPoliticalIncome(StateAbbreviation stateAbbreviation)  {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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