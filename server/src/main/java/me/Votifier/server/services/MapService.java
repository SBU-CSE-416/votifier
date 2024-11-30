package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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

    public ResponseEntity<Resource> colorHeatmapDemographic(
        ResponseEntity<Resource> precinctsBoundariesGeoJsonResponse, 
        ResponseEntity<Resource> precinctsPopulationGroupsJsonResponse, 
        RacialGroup selectedRacialGroup) {

        try {
            StringBuilder reusableJsonContentBuilder = new StringBuilder();

            Resource precinctsBoundariesBody = precinctsBoundariesGeoJsonResponse.getBody();
            InputStreamReader precinctsBoundariesBodyStream = new InputStreamReader(precinctsBoundariesBody.getInputStream());
            BufferedReader precinctsBoundariesBodyReader = new BufferedReader(precinctsBoundariesBodyStream);
            String precinctsBoundariesJsonString = constructJSONString(precinctsBoundariesBodyReader, reusableJsonContentBuilder);
            FeatureCollection precinctsBoundariesJson = JSON.parseObject(precinctsBoundariesJsonString, FeatureCollection.class);
            
            reusableJsonContentBuilder.setLength(0);

            Resource precinctsPopulationGroupsBody = precinctsPopulationGroupsJsonResponse.getBody();
            InputStreamReader precinctsPopulationGroupsBodyStream = new InputStreamReader(precinctsPopulationGroupsBody.getInputStream());
            BufferedReader precinctsPopulationGroupsBodyReader = new BufferedReader(precinctsPopulationGroupsBodyStream);
            String precinctsPopulationGroupsJsonString = constructJSONString(precinctsPopulationGroupsBodyReader, reusableJsonContentBuilder);
            JSONArray precinctsPopulationGroupsJson = JSON.parseObject(precinctsPopulationGroupsJsonString, JSONArray.class);

            String selectedRacialGroupIdentifier = selectedRacialGroup.getIdentifier();
            if(selectedRacialGroupIdentifier == null){
                throw new InvalidRacialGroupException();
            }
            Map<String, String> assignedPrecincts = new HashMap<>();
            List<JSONObject> precincts = precinctsPopulationGroupsJson.toJavaList(JSONObject.class);
            for(JSONObject precinct : precincts) {
                String uniquePrecinctId = precinct.getString("UNIQUE_ID");
                Bin assignedBin = null;
                double selectedRacialGroupPopulation = precinct.getDoubleValue(selectedRacialGroupIdentifier);
                double totalPopulation = precinct.getDoubleValue(TOTAL_POPULATION_IDENTIFIER);
                if(totalPopulation <= 0) {
                    assignedBin = loadedBins.get(1);
                }
                else {
                    int racialGroupPopulationPercentage = (int)((selectedRacialGroupPopulation/totalPopulation) * 100);
                    for(Bin bin : loadedBins.values()) {
                        if(bin.isInRange(racialGroupPopulationPercentage)) {
                            assignedBin = bin;
                            break;
                        }
                    }
                }
                if(assignedBin == null) {
                    throw new InvalidBinRangeException();
                }   
                String assignedPrecinctHexColor = assignedBin.getColor();
                assignedPrecincts.put(uniquePrecinctId, assignedPrecinctHexColor);
            }
            for(Feature precinct : precinctsBoundariesJson.getFeatures()) {
                Map<String, String> precinctProperties = precinct.getProperties();
                String precinctId = precinctProperties.get("UNIQUE_ID");
                precinctProperties.put("ASSIGNED_COLOR", assignedPrecincts.get(precinctId));
                precinct.setProperties(precinctProperties);
            }
            String stringJson = JSON.toJSONString(precinctsBoundariesJson, SerializerFeature.PrettyFormat);
            Resource resource = new ByteArrayResource(stringJson.getBytes());
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        }
        catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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