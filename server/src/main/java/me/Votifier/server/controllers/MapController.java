package me.Votifier.server.controllers;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import me.Votifier.server.model.StateViewLevel;
import me.Votifier.server.services.MapService;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/{fipsCode}/state")
    public ResponseEntity<Resource> getState(@PathVariable("fipsCode") int fipsCode) {
        return mapService.getGeoJsonFile(fipsCode, StateViewLevel.STATE);
    }

    @GetMapping("/{fipsCode}/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("fipsCode") int fipsCode) {
        return mapService.getGeoJsonFile(fipsCode, StateViewLevel.DISTRICTS);
    }

    @GetMapping("/{fipsCode}/precincts")
    public ResponseEntity<Resource> getPrecincts(@PathVariable("fipsCode") int fipsCode) {
        return mapService.getGeoJsonFile(fipsCode, StateViewLevel.PRECINCTS); 
    }
}