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

    private static final Map<Integer, String> stateIdToName = Map.of(
            24, "maryland",
            45, "south_carolina"
    );
    // @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @RequestMapping(value = "/{stateid}", method = RequestMethod.GET)
    // @GetMapping("")
    public ResponseEntity<Resource> getState(@PathVariable("stateid") int stateId) {
        String stateName = stateIdToName.get(stateId);
        if (stateName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Path filePath = Paths.get("data/states/" + stateName + "/geodata/" + stateName + "_state.geojson");
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
