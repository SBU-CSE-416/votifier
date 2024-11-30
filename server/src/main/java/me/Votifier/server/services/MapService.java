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
import me.Votifier.server.model.Bin;
import me.Votifier.server.model.BinsFeature;
import me.Votifier.server.model.configurations.bins.BinsConfig;
import me.Votifier.server.model.RacialGroup;

import me.Votifier.server.model.exceptions.InvalidBinRangeException;
import me.Votifier.server.model.exceptions.InvalidRacialGroupException;

import java.util.Arrays;

@Service
public class MapService {

    private static Map<BinsFeature, TreeMap<Integer, Bin>> loadedBins = new HashMap<>();

    @Autowired
    public MapService(BinsConfig binsConfig) {
        loadedBins.putAll(binsConfig.getAllBins());
    }

    private static final String TOTAL_POPULATION_IDENTIFIER = "TOT_POP22";

    // public void testRoute(){

    //     for(BinsFeature feature : loadedBins.keySet()){
    //         System.out.println(feature.name());
    //         TreeMap<Integer, Bin> tmap = loadedBins.get(feature);
    //         for (Map.Entry<Integer, Bin> entry : tmap.entrySet()) {
    //             Integer key = entry.getKey();  // Get the key (Integer)
    //             Bin bin = entry.getValue();    // Get the Bin object (value)
    //             // You can now work with the key and bin
    //             System.out.println("Key: " + key + ", Bin: " + bin.getBinNumber() + " | Color: " + bin.getColor() + " | Range: " + Arrays.toString(bin.getRange()));
    //         }
    //     }
    // }

    public ResponseEntity<Resource> colorHeatmapDemographic(
        ResponseEntity<Resource> precinctsBoundariesGeoJsonResponse, 
        ResponseEntity<Resource> precinctsPopulationGroupsJsonResponse, 
        RacialGroup selectedRacialGroup) {

        try {
            TreeMap<Integer, Bin> loadedHeatmapBins = loadedBins.get(BinsFeature.HEATMAP_DEMOGRAPHIC); 

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
                    assignedBin = loadedHeatmapBins.get(1);
                }
                else {
                    int racialGroupPopulationPercentage = (int)((selectedRacialGroupPopulation/totalPopulation) * 100);
                    for(Bin bin : loadedHeatmapBins.values()) {
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
            exception.printStackTrace();
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