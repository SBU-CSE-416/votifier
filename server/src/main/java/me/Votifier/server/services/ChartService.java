package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.MalformedURLException;

import me.Votifier.server.model.StateFipsCode;

@Service
public class ChartService {

    public ResponseEntity<Resource> getGeoJsonFile(int fipsCode) {
        Optional<StateFipsCode> optionalState = StateFipsCode.fromFipsCode(fipsCode);

        if (optionalState.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        StateFipsCode state = optionalState.get();
        try {
            Path filePath = resolveFilePath(state.getStateName());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok(resource);
            } 
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } 
        catch (MalformedURLException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private Path resolveFilePath(String stateName) {
        return Paths.get("data/states/", stateName, "/demographics/", (stateName + "_general.json"));
    }
}
