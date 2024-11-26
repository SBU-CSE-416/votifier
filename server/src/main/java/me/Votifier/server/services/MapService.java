package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.RacialGroup;

import me.Votifier.server.model.exceptions.InvalidBinRangeException;
import me.Votifier.server.model.exceptions.InvalidRacialGroupException;

@Service
public class MapService {

    private static final int RANGE_ARRAY_UPPER_BOUND_INDEX = 1;
    private static final String TOTAL_POPULATION_IDENTIFIER = "TOT_POP22";
    private static final String WHITE_POPULATION_IDENTIFIER = "WHT_NHSP22";
    private static final String BLACK_POPULATION_IDENTIFIER = "BLK_NHSP22";

    public ResponseEntity<Resource> colorHeatmapDemographic(
        ResponseEntity<Resource> precinctBoundariesGeoJSON, 
        ResponseEntity<Resource> precinctRacialGroupsPopulationJSON, 
        RacialGroup selectedRacialGroup) {
        // (GUI-4) Color the precinct heatmap appropriately based on the population percentage of racialGroup in each precinct
        try {
            NavigableMap<Integer, String> loadedBins = new TreeMap<Integer, String>();
            ObjectMapper mapper = new ObjectMapper();
            
            Resource res1 = precinctBoundariesGeoJSON.getBody();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(res1.getInputStream()));
            StringBuilder stringBuilder1 = new StringBuilder();
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                //System.out.println(line1);
                stringBuilder1.append(line1);
            }
            reader1.close();

            // Now you can safely parse the JSON string
            String geoJsonString = stringBuilder1.toString();
            JsonNode precinctBoundariesJSON = mapper.readTree(geoJsonString);
                
            JsonNode features = precinctBoundariesJSON.path("features");
            JsonNode binsFeature = features.get(features.size()-1);
            JsonNode binsProperties = binsFeature.path("properties");
            JsonNode binsArray = binsProperties.path("bins");
            for(JsonNode currentBin : binsArray) {
                int binUpperBound = currentBin.path("range").get(RANGE_ARRAY_UPPER_BOUND_INDEX).asInt();
                String binColorAsHex = currentBin.path("color").asText();
                loadedBins.put(binUpperBound, binColorAsHex);
                // System.out.println("Putting " + binUpperBound + ":" + binColorAsHex);
            }

            // for (Map.Entry<Integer, String> entry : loadedBins.entrySet()) {
            //     System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            // }


            Resource res2 = precinctRacialGroupsPopulationJSON.getBody();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(res2.getInputStream()));
            StringBuilder stringBuilder2 = new StringBuilder();
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                stringBuilder2.append(line2);
            }
            reader2.close();

            String jsonString = stringBuilder2.toString();

            JsonNode racialGroupsPopulationArray = mapper.readTree(jsonString);
            Map<String, JsonNode> racialGroupPopulations = new HashMap<>();
            for(JsonNode precinctRacialGroupPopulationsRow : racialGroupsPopulationArray) {
                String uniquePrecinctId = precinctRacialGroupPopulationsRow.path("UNIQUE_ID").asText();
                racialGroupPopulations.put(uniquePrecinctId, precinctRacialGroupPopulationsRow);
            }

            String racialDataRowIdentifier = null;
            switch(selectedRacialGroup){
                case WHITE:
                    racialDataRowIdentifier = WHITE_POPULATION_IDENTIFIER;
                    break;
                case BLACK:
                    racialDataRowIdentifier = BLACK_POPULATION_IDENTIFIER;
                    break;
                default:

                    throw new InvalidRacialGroupException();
            }

            for(JsonNode feature : precinctBoundariesJSON.path("features")) {
                JsonNode propertiesArray = feature.path("properties");
                String uniquePrecinctId = propertiesArray.path("UNIQUE_ID").asText();

                JsonNode precinctRacialGroupPopulationsRow = racialGroupPopulations.get(uniquePrecinctId);
                if(precinctRacialGroupPopulationsRow != null) {
                    double racialGroupPopulationInPrecinct = precinctRacialGroupPopulationsRow.path(racialDataRowIdentifier).asDouble();
                    double totalPopulationInPrecinct = precinctRacialGroupPopulationsRow.path(TOTAL_POPULATION_IDENTIFIER).asDouble();
                    System.out.println(racialDataRowIdentifier + ": " + racialGroupPopulationInPrecinct + "/" + totalPopulationInPrecinct);
                    Map.Entry<Integer, String> rangeEntry = null;
                    if(totalPopulationInPrecinct <= 9){
                        rangeEntry = loadedBins.floorEntry(9);
                    }
                    else {
                        int racialGroupPercentageInPrecinct = (int)((racialGroupPopulationInPrecinct/totalPopulationInPrecinct) * 100);
                        System.out.println("= Percent: " + racialGroupPercentageInPrecinct);
                        if(racialGroupPercentageInPrecinct <= 9){
                            rangeEntry = loadedBins.floorEntry(9);
                        }
                        else {
                            rangeEntry = loadedBins.floorEntry(racialGroupPercentageInPrecinct);
                        }
                    }
                    if(rangeEntry == null){
                        throw new InvalidBinRangeException();
                    }
                    String assignedPrecinctHexColor = rangeEntry.getValue();
                    ((ObjectNode) propertiesArray).put("assigned_color", assignedPrecinctHexColor);
                }
            }
            String stringJson = mapper.writeValueAsString(precinctBoundariesJSON);
            Resource resource = new ByteArrayResource(stringJson.getBytes());
            System.out.println("Encountered no errors :)");
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        }
        catch (Exception exception){
            System.out.println("Ran into errors...");
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Resource> colorHeatmapEconomicIncome(StateAbbreviation stateAbbreviation) {
        // TO DO: (GUI-5) Color the precinct heatmap appropriately based on the average household income in each precinct
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
}