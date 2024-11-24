package me.Votifier.server;

import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "states", key = "#fipsCode")
    @GetMapping("/{fipsCode}")
    public ResponseEntity<Resource> getState(@PathVariable("fipsCode") int fipsCode) {
        String stateName = fipsCodeToName.get(fipsCode);
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

    @GetMapping("/{fipsCode}/districts")
    public ResponseEntity<Resource> getDistricts(@PathVariable("fipsCode") int fipsCode) {
        String stateName = fipsCodeToName.get(fipsCode);
        if (stateName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            // Retrieve JSON file
            Path filePath = Paths.get("data/states/" + stateName + "/geodata/" + stateName + "_cd.geojson");
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

    @GetMapping("/{fipsCode}/demographics")
    public ResponseEntity<Resource> getDemographics(@PathVariable("fipsCode") int fipsCode) {
        String stateName = fipsCodeToName.get(fipsCode);
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
    @GetMapping("/{fipsCode}/precincts")
    public ResponseEntity<Resource> getPrecincts(@PathVariable("fipsCode") int fipsCode) {
        String stateName = fipsCodeToName.get(fipsCode);
            if (stateName == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            try {
                // Retrieve JSON file
                Path filePath = Paths.get("data/states/" + stateName + "/geodata/" + stateName + "_precincts.geojson");
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