package me.Votifier.server.controllers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.JSON;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.Votifier.server.model.documents.DistrictsBoundary;
import me.Votifier.server.model.documents.DistrictsSummary.DistrictData;
import me.Votifier.server.model.documents.PrecinctsBoundary;
import me.Votifier.server.model.documents.StateBoundary;
import me.Votifier.server.model.documents.HeatmapDocuments.EconomicHeatMap;
import me.Votifier.server.model.documents.HeatmapDocuments.RacialHeatMap;
import me.Votifier.server.model.documents.HeatmapDocuments.RegionTypeHeatMap;
import me.Votifier.server.model.exceptions.UnknownFileException;
import me.Votifier.server.model.repository.DistrictsBoundaryRepository;
import me.Votifier.server.model.repository.PrecinctsBoundaryRepository;
import me.Votifier.server.model.repository.StateBoundaryRepository;
import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.RacialGroup;
import me.Votifier.server.model.repository.RacialHeatMapRepository;
import me.Votifier.server.model.repository.EconomicHeatMapRepository;

import me.Votifier.server.model.repository.RegionTypeHeatMapRepository;

import me.Votifier.server.services.MapService;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/{stateAbbreviation}/boundary/state")
    public ResponseEntity<Resource> getState(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherStateBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/boundary/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherDistrictsBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/boundary/precincts")
    public ResponseEntity<Resource> getPrecincts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherPrecinctsBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/heatmap/demographic/{racialGroup}")
    public ResponseEntity<Resource> getHeatmapDemographic(
        @PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, 
        @PathVariable("racialGroup") RacialGroup racialGroup) {
    try {
        ResponseEntity<Resource> precinctRacialGroupsJSON = gatherPrecinctRacialGroupsFromCache(stateAbbreviation);

        if (precinctRacialGroupsJSON.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(precinctRacialGroupsJSON.getStatusCode());
        }

        RacialHeatMap racialHeatMap = parseResourceToRacialHeatMap(precinctRacialGroupsJSON.getBody());

        return mapService.colorHeatmapDemographic(racialHeatMap, racialGroup);
    } catch (IOException e) {
        System.out.println("Error parsing RacialHeatMap: " + e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    private RacialHeatMap parseResourceToRacialHeatMap(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        String json = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

        return JSON.parseObject(json, RacialHeatMap.class); // Deserialize to RacialHeatMap
    }

    @GetMapping("/{stateAbbreviation}/heatmap/economic-income")
    public ResponseEntity<Resource> getHeatmapEconomicIncome(
        @PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation
        ) {
        try{
            ResponseEntity<Resource> precinctEconomicGroupsJSON = gatherPrecinctEconomicGroupsFromCache(stateAbbreviation);

            if (precinctEconomicGroupsJSON.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>(precinctEconomicGroupsJSON.getStatusCode());
            }

            EconomicHeatMap precinctEconomicGroups = parseResourceToEconomicHeatMap(precinctEconomicGroupsJSON.getBody());
            return mapService.colorHeatmapEconomicIncome(precinctEconomicGroups);
        } catch (IOException e) {
            System.out.println("Error parsing EconomicHeatMap: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private EconomicHeatMap parseResourceToEconomicHeatMap(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        String json = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));
        
        return JSON.parseObject(json, EconomicHeatMap.class); // Deserialize to EconomicHeatMap
    }

    @GetMapping("/{stateAbbreviation}/heatmap/regions")
    public ResponseEntity<Resource> getHeatmapRegions(
        @PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation
        ) {
        try{
        ResponseEntity<Resource> precinctRegionGeoJSON = gatherPrecinctRegionsFromCache(stateAbbreviation);
        
        if (precinctRegionGeoJSON.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(precinctRegionGeoJSON.getStatusCode());
        }

        RegionTypeHeatMap precinctRegions = parseResourceToRegionTypeHeatMap(precinctRegionGeoJSON.getBody());
        return mapService.colorHeatmapRegions(precinctRegions);
        } catch (IOException e) {
            System.out.println("Error parsing RegionTypeHeatMap: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private RegionTypeHeatMap parseResourceToRegionTypeHeatMap(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        String json = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));
        
        return JSON.parseObject(json, RegionTypeHeatMap.class); // Deserialize to RegionTypeHeatMap
    }
    

    @GetMapping("/{stateAbbreviation}/heatmap/economic-poverty")
    public ResponseEntity<Resource> getHeatmapEconomicPoverty(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        try {
        ResponseEntity<Resource> precinctEconomicGroupsJSON = gatherPrecinctEconomicGroupsFromCache(stateAbbreviation);

        if (precinctEconomicGroupsJSON.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(precinctEconomicGroupsJSON.getStatusCode());
        }

        EconomicHeatMap precinctEconomicGroups = parseResourceToEconomicHeatMap(precinctEconomicGroupsJSON.getBody());
        return mapService.colorHeatmapEconomicPoverty(precinctEconomicGroups);
        } catch (IOException e) {
            System.out.println("Error parsing EconomicHeatMap: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Autowired
    private PrecinctsBoundaryRepository precinctsBoundaryRepository;

    @Cacheable(value = "statePrecincts", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherPrecinctsBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            List<PrecinctsBoundary> boundaries = precinctsBoundaryRepository.findAllByNAME(stateAbbreviation.getFullStateName());

            if (boundaries == null || boundaries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Create a GeoJSON FeatureCollection
            Map<String, Object> featureCollection = new HashMap<>();
            featureCollection.put("type", "FeatureCollection");

            // Convert boundaries to a list of features
            List<Map<String, Object>> features = boundaries.stream().map(boundary -> {
                Map<String, Object> feature = new HashMap<>();
                feature.put("type", boundary.getType());
                feature.put("properties", boundary.getProperties());
                feature.put("geometry", boundary.getGeometry());
                return feature;
            }).toList();

            featureCollection.put("features", features);

            String jsonResponse = com.alibaba.fastjson2.JSON.toJSONString(
                featureCollection,
                com.alibaba.fastjson2.JSONWriter.Feature.LargeObject 
            );

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json"))
                    .body(resource);

        } 
        catch (Exception exception) {
            System.out.println("Error fetching or serializing precincts boundary: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private StateBoundaryRepository stateboundaryRepository;
    @Cacheable(value = "stateBoundaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherStateBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            StateBoundary stateboundary = stateboundaryRepository.findByNAME(stateAbbreviation.getFullStateName());
    
            if (stateboundary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(stateboundary);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
        
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        } 
        catch (Exception exception) {
            System.out.println("Error fetching or serializing state boundary: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private DistrictsBoundaryRepository districtsBoundaryRepository;
    @Cacheable(value = "stateDistricts", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherDistrictsBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            DistrictsBoundary boundary = districtsBoundaryRepository.findByNAME(stateAbbreviation.getFullStateName());
    
            if (boundary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(boundary);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        } 
        catch (Exception exception) {
            System.out.println("Error fetching or serializing state boundary: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public ResponseEntity<Resource> gatherPrecinctRacialGroupsFromLocal(StateAbbreviation stateAbbreviation){
        if(stateAbbreviation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path filePath = null;
            switch(stateAbbreviation){
                case SC:
                    filePath = Paths.get("data/processed_individual/sc_precinct_racial_groups.json");
                    break;
                case MD:
                    filePath = Paths.get("data/processed_individual/md_precinct_racial_groups.json");
                    break;
                default:
                    filePath = Paths.get("data/processed_individual/unknown_precinct_racial_groups.json");
                    break;
            }
            Resource resource = getResourceFromLocal(filePath);
            return ResponseEntity.ok(resource);
        }
        catch (UnknownFileException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    private RacialHeatMapRepository racialHeatMapRepository;

    @Cacheable(value = "stateRacialGroups", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherPrecinctRacialGroupsFromCache(StateAbbreviation stateAbbreviation){
        try{
            String stateName = stateAbbreviation.getFullStateName();
            RacialHeatMap racialHeatMap = racialHeatMapRepository.findByName(stateName);

            if(racialHeatMap == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(racialHeatMap, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception exception) {
            System.out.println("Error fetching or serializing precincts racial data: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired EconomicHeatMapRepository economicHeatMapRepository;

    @Cacheable(value = "stateEconomicGroups", key = "#stateAbbreviation")

    public ResponseEntity<Resource> gatherPrecinctEconomicGroupsFromCache(StateAbbreviation stateAbbreviation) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            EconomicHeatMap economicHeatMap = economicHeatMapRepository.findByName(stateName);

            if (economicHeatMap == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(economicHeatMap, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception exception) {
            System.out.println("Error fetching or serializing precincts economic data: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired RegionTypeHeatMapRepository regionTypeHeatMapRepository;

    @Cacheable(value = "stateRegions", key = "#stateAbbreviation")

    public ResponseEntity<Resource> gatherPrecinctRegionsFromCache(StateAbbreviation stateAbbreviation) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            RegionTypeHeatMap regionHeatMap = regionTypeHeatMapRepository.findByName(stateName);

            if (regionHeatMap == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(regionHeatMap, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception exception) {
            System.out.println("Error fetching or serializing precincts economic data: " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public ResponseEntity<Resource> gatherPrecinctEconomicGroupsFromLocal(StateAbbreviation stateAbbreviation){
        if(stateAbbreviation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path filePath = null;
            switch(stateAbbreviation){
                case SC:
                    filePath = Paths.get("data/states/south_carolina/economic/south_carolina_precincts_household_income_fixed.json");
                    break;
                case MD:
                    filePath = Paths.get("data/states/maryland/economic/maryland_precincts_household_income_fixed.json");
                    break;
                default:
                    filePath = Paths.get("data/states/unknown/economic/unknown_precincts_household_income_fixed.json");
                    break;
            }
            Resource resource = getResourceFromLocal(filePath);
            return ResponseEntity.ok(resource);
        }
        catch (UnknownFileException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public Resource getResourceFromLocal(Path filePath) throws UnknownFileException {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            else {
                throw new UnknownFileException();
            }
        }
        catch (MalformedURLException exception) {
            throw new UnknownFileException();
        }
    }
}