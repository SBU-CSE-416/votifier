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
    private static final String TOTAL_POPULATION_IDENTIFIER = "TOTAL_POPULATION";
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


    public ResponseEntity<Resource> colorHeatmapEconomicIncome(
        ResponseEntity<Resource> precinctsBoundariesGeoJsonResponse, 
        ResponseEntity<Resource> precinctsEconomicGroupsJsonResponse) {
        try {
            Map<Integer, Bin> loadedHeatmapBins = loadedBins.get(FeatureName.HEATMAP_ECONOMIC_INCOME); 
            final int BIN_COLOR_INDEX = 0;
            final String UNIQUE_ID_LABEL = "UNIQUE_ID";
            final String COLOR_LABEL = "ASSIGNED_COLOR";
            final String INCOME_LABEL = "AVERAGE_HOUSEHOLD_INCOME";

            StringBuilder reusableJsonContentBuilder = new StringBuilder();

            Resource precinctsBoundariesBody = precinctsBoundariesGeoJsonResponse.getBody();
            InputStreamReader precinctsBoundariesBodyStream = new InputStreamReader(precinctsBoundariesBody.getInputStream());
            BufferedReader precinctsBoundariesBodyReader = new BufferedReader(precinctsBoundariesBodyStream);
            String precinctsBoundariesJsonString = constructJSONString(precinctsBoundariesBodyReader, reusableJsonContentBuilder);
            FeatureCollection precinctsBoundariesJson = JSON.parseObject(precinctsBoundariesJsonString, FeatureCollection.class);
            
            reusableJsonContentBuilder.setLength(0);

            Resource precinctsEconomicGroupsBody = precinctsEconomicGroupsJsonResponse.getBody();
            InputStreamReader precinctsEconomicGroupsBodyStream = new InputStreamReader(precinctsEconomicGroupsBody.getInputStream());
            BufferedReader precinctsEconomicGroupsBodyReader = new BufferedReader(precinctsEconomicGroupsBodyStream);
            String precinctsEconomicGroupsJsonString = constructJSONString(precinctsEconomicGroupsBodyReader, reusableJsonContentBuilder);
            JSONArray precinctsEconomicGroupsJson = JSON.parseObject(precinctsEconomicGroupsJsonString, JSONArray.class);

            Map<String, String> assignedPrecinctsColors = new HashMap<>();
            Map<String, String> assignedPrecinctsIncome = new HashMap<>();
            List<JSONObject> precincts = precinctsEconomicGroupsJson.toJavaList(JSONObject.class);

            for(JSONObject precinct : precincts) {
                String uniquePrecinctId = precinct.getString(UNIQUE_ID_LABEL);
                Bin assignedBin = null;
                double totalAccumulatedIncomeSum = 0.0;
                int estimatedAverageHouseholdIncome = 0;
                double totalHouseholds = precinct.getDoubleValue(TOTAL_HOUSEHOLDS_IDENTIFIER);

                if(totalHouseholds <= 0.0) {
                    assignedBin = loadedHeatmapBins.get(1);
                }
                else {
                    for(String potentialHouseholdsBracketKey : precinct.keySet()) {
                        int incomeBracketIndex = economicBinStringToIndexMapping.getOrDefault(potentialHouseholdsBracketKey, INVALID_MAPPING_INDEX);
                        if(incomeBracketIndex != INVALID_MAPPING_INDEX) {
                            int[] binRange = loadedHeatmapBins.get(incomeBracketIndex).getBinRange();
                            double midpointOfRangeValues = 0.0;
                            if(binRange[1] == INFINITE_BIN_RANGE_INDEX) {
                                midpointOfRangeValues = binRange[0];
                            }
                            else {
                                midpointOfRangeValues = (binRange[0] + binRange[1]) / 2;
                            }
                            double householdsForIncomeBracket = precinct.getDoubleValue(potentialHouseholdsBracketKey);
                            totalAccumulatedIncomeSum += (int) (midpointOfRangeValues * householdsForIncomeBracket);
                        }
                    }
                    estimatedAverageHouseholdIncome = (int) (totalAccumulatedIncomeSum / totalHouseholds);
                    for(Bin bin : loadedHeatmapBins.values()) {
                        if(bin.isInBinRange(estimatedAverageHouseholdIncome)) {
                            assignedBin = bin;
                            break;
                        }
                    }
                }
                if(assignedBin == null) {
                    throw new InvalidBinRangeException();
                }   
                String assignedPrecinctHexColor = assignedBin.getBinColors()[BIN_COLOR_INDEX];
                String assignedPrecinctAverageHouseholdIncome = String.valueOf(estimatedAverageHouseholdIncome);
                assignedPrecinctsColors.put(uniquePrecinctId, assignedPrecinctHexColor);
                assignedPrecinctsIncome.put(uniquePrecinctId, assignedPrecinctAverageHouseholdIncome);
            }
            for(Feature precinct : precinctsBoundariesJson.getFeatures()) {
                Map<String, String> precinctProperties = precinct.getProperties();
                String precinctId = precinctProperties.get(UNIQUE_ID_LABEL);
                precinctProperties.put(COLOR_LABEL, assignedPrecinctsColors.get(precinctId));
                precinctProperties.put(INCOME_LABEL, assignedPrecinctsIncome.get(precinctId));
                precinct.setProperties(precinctProperties);
            }
            String stringJson = JSON.toJSONString(precinctsBoundariesJson, SerializerFeature.PrettyFormat);
            Resource resource = new ByteArrayResource(stringJson.getBytes());
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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