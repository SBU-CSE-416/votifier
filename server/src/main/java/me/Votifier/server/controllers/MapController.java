package me.Votifier.server.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import me.Votifier.server.services.MapService;

import me.Votifier.server.model.StateViewLevel;
import me.Votifier.server.model.exceptions.UnknownFileException;
import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.RacialGroup;

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
        return gatherBoundaryDataFromLocal(stateAbbreviation, StateViewLevel.STATE);
    }

    @GetMapping("/{stateAbbreviation}/boundary/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherBoundaryDataFromLocal(stateAbbreviation, StateViewLevel.DISTRICTS);
    }

    @GetMapping("/{stateAbbreviation}/boundary/precincts")
    public ResponseEntity<Resource> getPrecincts(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherBoundaryDataFromLocal(stateAbbreviation, StateViewLevel.PRECINCTS);
    }

    @GetMapping("/{stateAbbreviation}/heatmap/demographic/{racialGroup}")
    public ResponseEntity<Resource> getHeatmapDemographic(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup){
        // Note: These two methods will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        ResponseEntity<Resource> precinctBoundaryGeoJSON = gatherPrecinctBoundariesFromLocal(stateAbbreviation);
        ResponseEntity<Resource> precinctRacialGroupsJSON = gatherPrecinctRacialGroupsFromLocal(stateAbbreviation);
        return mapService.colorHeatmapDemographic(precinctBoundaryGeoJSON, precinctRacialGroupsJSON, racialGroup);
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
                    filePath = Paths.get("data/processed_individual/sc_precinct_boundaries_with_bins.geojson");
                    break;
                case MD:
                    filePath = Paths.get("data/processed_individual/md_precinct_boundaries_with_bins.geojson");
                    break;
                default:
                    filePath = Paths.get("data/processed_individual/unknown_precinct_boundaries_with_bins.geojson");
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

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public ResponseEntity<Resource> gatherBoundaryDataFromLocal(StateAbbreviation stateAbbreviation, StateViewLevel stateViewLevel) {
        if(stateAbbreviation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path filePath = resolveFilePath(stateAbbreviation.getFullStateName(), stateViewLevel);
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
    
    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    private Path resolveFilePath(String fullStateName, StateViewLevel stateViewLevel) {
        String fileExtensionSegment = stateViewLevel.getFileExtensionSegment();
        return Paths.get("data/states/", fullStateName, "/geodata/", (fullStateName + fileExtensionSegment));
    }
}