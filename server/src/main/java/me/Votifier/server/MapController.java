package me.Votifier.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MapController {

    @GetMapping
    public String getMapping() {
        return "Map route response";
    }
}
