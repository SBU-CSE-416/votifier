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
import me.Votifier.server.model.StateViewLevel;

@Service
public class MapService {

    public ResponseEntity<Resource> getGeoJsonFile(int fipsCode, StateViewLevel viewLevel) {
        Optional<StateFipsCode> optionalState = StateFipsCode.fromFipsCode(fipsCode);

        if (optionalState.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        StateFipsCode state = optionalState.get();
        try {
            Path filePath = resolveFilePath(state.getStateName(), viewLevel);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok(resource);
            } 
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
        catch (MalformedURLException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private Path resolveFilePath(String stateName, StateViewLevel viewLevel) {
        String fileExtensionSegment = viewLevel.getFileExtensionSegment();
        return Paths.get("data/states/", stateName, "/geodata/", (stateName + fileExtensionSegment));
    }
}