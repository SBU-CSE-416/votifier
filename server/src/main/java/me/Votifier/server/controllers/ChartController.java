package me.Votifier.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import me.Votifier.server.services.ChartService;


@RestController
@RequestMapping("/api/charts")
public class ChartController {

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/{fipsCode}/summary")
    public ResponseEntity<Resource> getStateSummary(@PathVariable("fipsCode") int fipsCode) {
        return chartService.getGeoJsonFile(fipsCode);
    }
}