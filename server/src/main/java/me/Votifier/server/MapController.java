package me.Votifier.server;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MapController {

    private static final Map<Integer, String> fipsCodeToName = Map.of(
            24, "maryland",
            45, "south_carolina"
    );

    @GetMapping("/{fips_code}")
    public ResponseEntity<Resource> getState(@PathVariable("fips_code") int fips_code) {
        String stateName = fipsCodeToName.get(fips_code);
        if (stateName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            // Retrieve JSON file
            Path filePath = Paths.get("data/states/" + stateName + "/geodata/" + stateName + "_state.geojson");
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
        catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{fips_code}/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("fips_code") int fips_code) {
        String stateName = fipsCodeToName.get(fips_code);
        if (stateName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            // Retrieve JSON file
            Path filePath = Paths.get("data/states/" + stateName + "/geodata/" + stateName + "_cds.geojson");
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
        catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{fips_code}/demographics")
    public ResponseEntity<Resource> getDemographics(@PathVariable("fips_code") int fips_code) {
        String stateName = fipsCodeToName.get(fips_code);
        if (stateName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            // Retrieve JSON file
            Path filePath = Paths.get("data/states/" + stateName + "/demographics/" + stateName + "_general.json");
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
        catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}