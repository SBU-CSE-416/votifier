package me.Votifier.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.Votifier.server.services.DataService;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.StateSummary;
import me.Votifier.server.model.exceptions.UnknownFileException;
import me.Votifier.server.repository.StateSummaryRepository;

import org.springframework.cache.annotation.Cacheable;

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
        return gatherSummaryDataFromCache(stateAbbreviation);
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    @Autowired
    private StateSummaryRepository stateSummaryRepository;

    @Cacheable(value = "stateSummaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherSummaryDataFromCache(StateAbbreviation stateAbbreviation) {
        // Print state abbreviation details
        System.out.println("State Abbreviation: " + stateAbbreviation);
        System.out.println("State Abbreviation Full Name: " + stateAbbreviation.getFullStateName());

        try {
            // Fetch the document using the NAME field
            StateSummary stateSummary = stateSummaryRepository.findByNAME(stateAbbreviation.getFullStateName());

            if (stateSummary == null) {
                System.out.println("No state summary found for: " + stateAbbreviation.getFullStateName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Convert the StateSummary object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(stateSummary);

            // Convert JSON response into a Resource
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            // Return the resource with appropriate content type
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing state summary: " + e.getMessage());
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
    private Path resolveFilePath(String fullStateName) {
        System.out.println(Paths.get("data/states/", fullStateName, "/summary/", (fullStateName + "_summary.json")));
        return Paths.get("data/states/", fullStateName, "/summary/", (fullStateName + "_summary.json"));
    }
}