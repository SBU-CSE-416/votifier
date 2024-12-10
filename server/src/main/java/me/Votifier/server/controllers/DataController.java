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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.TypeReference;

import me.Votifier.server.services.DataService;
import me.Votifier.server.model.RacialGroup;
import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.repository.StateSummaryRepository;
import me.Votifier.server.model.repository.DistrictsSummaryRepository;
import me.Votifier.server.model.documents.DistrictsSummary.DistrictData;
import me.Votifier.server.model.documents.GinglesAnalysis;
import me.Votifier.server.model.documents.StateSummary;
import me.Votifier.server.model.exceptions.UnknownFileException;

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
        return gatherSummaryDataFromCache(stateAbbreviation);
    }
    @GetMapping("/{stateAbbreviation}/districts/summary")
    public ResponseEntity<Resource> getDistrictsSummary(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherDistrictsDataFromCache(stateAbbreviation);
    }
    @GetMapping("/{stateAbbreviation}/gingles/demographics/{racialGroup}")
    public ResponseEntity<Resource> getGinglesAnalysisByRace(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup) {
        return gatherGinglesDataFromCache(stateAbbreviation, racialGroup);
    }

    @Autowired
    private StateSummaryRepository stateSummaryRepository;

    @Cacheable(value = "stateSummaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherSummaryDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            StateSummary stateSummary = stateSummaryRepository.findByNAME(stateAbbreviation.getFullStateName());

            if (stateSummary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(stateSummary);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing state summary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private DistrictsSummaryRepository districtsSummaryRepository;

    @Cacheable(value = "districtsSummaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherDistrictsDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
    
            me.Votifier.server.model.documents.DistrictsSummary districtsSummary = districtsSummaryRepository.findByName(stateName);
    
            if (districtsSummary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            
            String jsonResponse = JSON.toJSONString(districtsSummary, config);
    
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing districts summary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Autowired
    private me.Votifier.server.model.repository.GinglesRepository ginglesRepository;

    @Cacheable(value = "ginglesAnalysis", key = "#stateAbbreviation + '-' + #racialGroup.ginglesIdentifer")
    public ResponseEntity<Resource> gatherGinglesDataFromCache(StateAbbreviation stateAbbreviation, RacialGroup racialGroup) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String racialGroupName = racialGroup.getGinglesIdentifer();
    
            me.Votifier.server.model.documents.GinglesAnalysis ginglesAnalysis = ginglesRepository.findByNameAndRace(stateName, racialGroupName);
    
            if (ginglesAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            String jsonResponse = JSON.toJSONString(ginglesAnalysis, config);
            GinglesAnalysis analysis = JSON.parseObject(
                jsonResponse,
                new TypeReference<GinglesAnalysis>() {}
            );
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing gingles analysis: " + e.getMessage());
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
}