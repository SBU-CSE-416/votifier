package me.Votifier.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import me.Votifier.server.services.DataService;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.exceptions.UnknownFileException;


@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{stateAbbreviation}/summary")
    public ResponseEntity<Resource> getStateSummary(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        // Note: This method will eventually be changed, since we will be accessing the cache/database for this data instead of locally
        return gatherSummaryDataFromLocal(stateAbbreviation);
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public ResponseEntity<Resource> gatherSummaryDataFromLocal(StateAbbreviation stateAbbreviation) {
        if(stateAbbreviation == null) {
            System.out.println("(!) Invalid state");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Path filePath = resolveFilePath(stateAbbreviation.getFullStateName());
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
    private Path resolveFilePath(String fullStateName) {
        return Paths.get("data/states/", fullStateName, "/demographics/", (fullStateName + "_general.json"));
    }
}