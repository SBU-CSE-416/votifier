package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.RacialGroup;

@Service
public class MapService {

    public ResponseEntity<Resource> colorHeatmapDemographic(StateAbbreviation stateAbbreviation, RacialGroup RacialGroup) {
        // TO DO: (GUI-4) Color the precinct heatmap appropriately based on the population percentage of racialGroup in each precinct
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapEconomicIncome(StateAbbreviation stateAbbreviation) {
        // TO DO: (GUI-5) Color the precinct heatmap appropriately based on the average household income in each precinct
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapEconomicRegions(StateAbbreviation stateAbbreviation) {
        // TO DO: (GUI-5) Color the precinct heatmap appropriately based on the threshold-defined region type in each precinct
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapEconomicPoverty(StateAbbreviation stateAbbreviation) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> colorHeatmapPoliticalIncome(StateAbbreviation stateAbbreviation)  {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}