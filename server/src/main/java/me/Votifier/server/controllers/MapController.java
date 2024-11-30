package me.Votifier.server.controllers;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.Cache;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import me.Votifier.server.services.MapService;

import me.Votifier.server.model.StateViewLevel;
import me.Votifier.server.model.exceptions.UnknownFileException;
import me.Votifier.server.repository.StateBoundaryRepository;
import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.StateBoundary;
import me.Votifier.server.repository.DistrictsBoundaryRepository;
import me.Votifier.server.model.DistrictsBoundary;
import me.Votifier.server.model.PrecinctsBoundary;
import me.Votifier.server.repository.PrecinctsBoundaryRepository;
import me.Votifier.server.model.RacialGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;


    public MapController(MapService mapService) {
        this.mapService = mapService;

    }

    @GetMapping("/{stateAbbreviation}/boundary/state")
    public ResponseEntity<Resource> getState(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherStateBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/boundary/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherDistrictsBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/boundary/precincts")
    public ResponseEntity<Resource> getPrecincts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherPrecinctsBoundaryDataFromCache(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/heatmap/demographic/{racialGroup}")
    public ResponseEntity<Resource> getHeatmapDemographic(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup){
        // Note: These two methods will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        ResponseEntity<Resource> precinctBoundaryGeoJSON = gatherPrecinctBoundariesFromLocal(stateAbbreviation);
        ResponseEntity<Resource> precinctRacialGroupsJSON = gatherPrecinctsBoundaryDataFromCache(stateAbbreviation);
        return null;
    }

    @GetMapping("/{stateAbbreviation}/heatmap/economic-income")
    public ResponseEntity<Resource> getHeatmapEconomicIncome(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation){
        return mapService.colorHeatmapEconomicIncome(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/heatmap/economic-regions")
    public ResponseEntity<Resource> getHeatmapEconomicRegions(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation){
        return mapService.colorHeatmapEconomicRegions(stateAbbreviation);
    }

    @GetMapping("/{stateAbbreviation}/heatmap/economic-poverty")
    public ResponseEntity<Resource> getHeatmapEconomicPoverty(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation){
        return mapService.colorHeatmapEconomicPoverty(stateAbbreviation);
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public ResponseEntity<Resource> gatherPrecinctBoundariesFromLocal(StateAbbreviation stateAbbreviation){
        if(stateAbbreviation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path filePath = null;
            switch(stateAbbreviation){
                case SC:
                    filePath = Paths.get("data/processed_individual/sc_precinct_boundaries.geojson");
                    break;
                case MD:
                    filePath = Paths.get("data/processed_individual/md_precinct_boundaries.geojson");
                    break;
                default:
                    filePath = Paths.get("data/processed_individual/unknown_precinct_boundaries.geojson");
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
    @Autowired
    private PrecinctsBoundaryRepository precinctsBoundaryRepository;

    @Cacheable(value = "statePrecincts", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherPrecinctsBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
        System.out.println("State Abbreviation: " + stateAbbreviation);
        System.out.println("State Abbreviation Full Name: " + stateAbbreviation.getFullStateName());
        try {
            // Fetch the documents using the NAME field
            List<PrecinctsBoundary> boundaries = precinctsBoundaryRepository.findAllByNAME(stateAbbreviation.getFullStateName());

            if (boundaries == null || boundaries.isEmpty()) {
                System.out.println("No precincts boundary found for: " + stateAbbreviation.getFullStateName());
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

            // Convert the FeatureCollection to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(featureCollection);

            // Convert JSON response to a Resource
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json"))
                    .body(resource);

        } catch (Exception e) {
            System.out.println("Error fetching or serializing precincts boundary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



        // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
        @Autowired
        private StateBoundaryRepository stateboundaryRepository;
        @Cacheable(value = "stateBoundaries", key = "#stateAbbreviation")
        public ResponseEntity<Resource> gatherStateBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
            //print stateAbbreviation
            System.out.println("State Abbreviation: " + stateAbbreviation);
            System.out.println("State Abbreviation Full Name: " + stateAbbreviation.getFullStateName());
            try {
                // Fetch the document using the NAME field
                StateBoundary stateboundary = stateboundaryRepository.findByNAME(stateAbbreviation.getFullStateName());
        
                if (stateboundary == null) {
                    System.out.println("No state boundary found for: " + stateAbbreviation.getFullStateName());
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        
                // Convert to JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(stateboundary);
                Resource resource = new ByteArrayResource(jsonResponse.getBytes());
        
                return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
            } catch (Exception e) {
                System.out.println("Error fetching or serializing state boundary: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @Autowired
    private DistrictsBoundaryRepository districtsBoundaryRepository;
    @Cacheable(value = "stateDistricts", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherDistrictsBoundaryDataFromCache(StateAbbreviation stateAbbreviation) {
        //print stateAbbreviation
        System.out.println("State Abbreviation: " + stateAbbreviation);
        System.out.println("State Abbreviation Full Name: " + stateAbbreviation.getFullStateName());
        try {
            // Fetch the document using the NAME field
            DistrictsBoundary boundary = districtsBoundaryRepository.findByNAME(stateAbbreviation.getFullStateName());
    
            if (boundary == null) {
                System.out.println("No districts boundary found for: " + stateAbbreviation.getFullStateName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    
            // Convert to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            
            String jsonResponse = objectMapper.writeValueAsString(boundary);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK).contentType(org.springframework.http.MediaType.parseMediaType("application/geo+json")).body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing state boundary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
    
    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    private Path resolveFilePath(String fullStateName, StateViewLevel stateViewLevel) {
        String fileExtensionSegment = stateViewLevel.getFileExtensionSegment();
        return Paths.get("data/states/", fullStateName, "/geodata/", (fullStateName + fileExtensionSegment));
    }
}