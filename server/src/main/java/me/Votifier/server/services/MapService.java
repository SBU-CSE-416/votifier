package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.Bin;
import me.Votifier.server.model.RacialGroup;

import me.Votifier.server.model.exceptions.InvalidBinRangeException;
import me.Votifier.server.model.exceptions.InvalidRacialGroupException;

@Service
public class MapService {

    
    private static final TreeMap<Integer, Bin> loadedBins = new TreeMap<>();

    public MapService() {
        loadedBins.put(Integer.valueOf(1), new Bin(1,new int[]{0,9},"#fff6f7"));
        loadedBins.put(Integer.valueOf(2), new Bin(1,new int[]{10,19}, "#fde1e0"));
        loadedBins.put(Integer.valueOf(3), new Bin(1,new int[]{20,29}, "#ffc3bf"));
        loadedBins.put(Integer.valueOf(4), new Bin(1,new int[]{30,39}, "#fb9eb8"));
        loadedBins.put(Integer.valueOf(5), new Bin(1,new int[]{40,49}, "#f768a2"));
        loadedBins.put(Integer.valueOf(6), new Bin(1,new int[]{50,59}, "#df3595"));
        loadedBins.put(Integer.valueOf(7), new Bin(1,new int[]{60,69}, "#b10085"));
        loadedBins.put(Integer.valueOf(8), new Bin(1,new int[]{70,79}, "#7c007a"));
        loadedBins.put(Integer.valueOf(9), new Bin(1,new int[]{80,89}, "#51006d"));
        loadedBins.put(Integer.valueOf(10), new Bin(1,new int[]{90,100}, "#1c0227"));
    }

    private static final String TOTAL_POPULATION_IDENTIFIER = "TOT_POP22";
    private static final String WHITE_POPULATION_IDENTIFIER = "WHT_NHSP22";
    private static final String BLACK_POPULATION_IDENTIFIER = "BLK_NHSP22";
    private static final String HISPANIC_LATINO_POPULATION_IDENTIFIER = "HIS_POP22";
    private static final String ASIAN_POPULATION_IDENTIFIER = "ASN_NHSP22";
    private static final String PACIFIC_ISLANDER_POPULATION_IDENTIFIER = "HPI_NHSP22";
    private static final String NATIVE_AMERICAN_POPULATION_IDENTIFIER = "AIA_NHSP22";

    

    public ResponseEntity<Resource> colorHeatmapDemographic(
        ResponseEntity<Resource> precinctBoundariesGeoJSON, 
        ResponseEntity<Resource> precinctRacialGroupsPopulationJSON, 
        RacialGroup selectedRacialGroup) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource responseBodyPrecinctBoundaries = precinctBoundariesGeoJSON.getBody();
            BufferedReader responseBodyPrecinctBoundariesReader = new BufferedReader(new InputStreamReader(responseBodyPrecinctBoundaries.getInputStream()));
            StringBuilder jsonContent = new StringBuilder();
            String currentJsonLine = "";
            while ((currentJsonLine = responseBodyPrecinctBoundariesReader.readLine()) != null) {
                jsonContent.append(currentJsonLine);
            }
            responseBodyPrecinctBoundariesReader.close();
            String precinctBoundariesJsonString = jsonContent.toString();
            JsonNode precinctBoundariesJsonArray = mapper.readTree(precinctBoundariesJsonString);

            jsonContent.setLength(0);
            currentJsonLine = "";

            Resource responseBodyPrecinctRacialGroups = precinctRacialGroupsPopulationJSON.getBody();
            BufferedReader responseBodyPrecinctRacialGroupsReader = new BufferedReader(new InputStreamReader(responseBodyPrecinctRacialGroups.getInputStream()));
            while ((currentJsonLine = responseBodyPrecinctRacialGroupsReader.readLine()) != null) {
                jsonContent.append(currentJsonLine);
            }
            responseBodyPrecinctRacialGroupsReader.close();
            String precinctRacialGroupsJsonString = jsonContent.toString();
            JsonNode racialGroupsJsonArray = mapper.readTree(precinctRacialGroupsJsonString);
            
            String racialDataRowIdentifier = null;
            switch(selectedRacialGroup){
                case WHITE:
                    racialDataRowIdentifier = WHITE_POPULATION_IDENTIFIER;
                    break;
                case BLACK:
                    racialDataRowIdentifier = BLACK_POPULATION_IDENTIFIER;
                    break;
                case HISPANIC_LATINO:
                    racialDataRowIdentifier = HISPANIC_LATINO_POPULATION_IDENTIFIER;
                    break;
                case ASIAN:
                    racialDataRowIdentifier = ASIAN_POPULATION_IDENTIFIER;
                    break;
                case PACIFIC_ISLANDER:
                    racialDataRowIdentifier = PACIFIC_ISLANDER_POPULATION_IDENTIFIER;
                    break;
                case NATIVE_AMERICAN:
                    racialDataRowIdentifier = NATIVE_AMERICAN_POPULATION_IDENTIFIER;
                    break;
                default:
                    throw new InvalidRacialGroupException();
            }
            HashMap<String, String> assignedPrecincts = new HashMap<>();
            for(JsonNode precinctRacialGroupsEntry : racialGroupsJsonArray) {
                String uniquePrecinctId = precinctRacialGroupsEntry.path("UNIQUE_ID").asText();
                Bin assignedBin = null;
                double racialGroupPopulationInPrecinct = precinctRacialGroupsEntry.path(racialDataRowIdentifier).asDouble();
                double totalPopulationInPrecinct = precinctRacialGroupsEntry.path(TOTAL_POPULATION_IDENTIFIER).asDouble();
                if(totalPopulationInPrecinct <= 0) {
                    assignedBin = loadedBins.get(1);
                }
                else {
                    int racialGroupPercentageInPrecinct = (int)((racialGroupPopulationInPrecinct/totalPopulationInPrecinct) * 100);
                    for(Bin bin : loadedBins.values()){
                        if(bin.isInRange(racialGroupPercentageInPrecinct)){
                            assignedBin = loadedBins.get(bin.getBinNumber());
                            break;
                        }
                    }
                }
                if(assignedBin == null){
                    throw new InvalidBinRangeException();
                }
                String assignedPrecinctHexColor = assignedBin.getColor();
                assignedPrecincts.put(uniquePrecinctId, assignedPrecinctHexColor);
            }
            for(JsonNode precinctFeature : precinctBoundariesJsonArray.path("features")) {
                JsonNode propertiesArray = precinctFeature.path("properties");
                String uniquePrecinctId = propertiesArray.path("UNIQUE_ID").asText();
                ((ObjectNode) propertiesArray).put("assigned_color", assignedPrecincts.get(uniquePrecinctId));
            }
            String stringJson = mapper.writeValueAsString(precinctBoundariesJsonArray);
            Resource resource = new ByteArrayResource(stringJson.getBytes());
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        }
        catch (Exception exception){
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