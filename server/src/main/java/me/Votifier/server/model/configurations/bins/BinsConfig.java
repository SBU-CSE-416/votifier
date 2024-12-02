package me.Votifier.server.model.configurations.bins;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.HashMap;

import me.Votifier.server.model.configurations.FeatureName;

@Configuration
@PropertySource("classpath:configurations/bins/heatmap-demographic.properties")
@PropertySource("classpath:configurations/bins/heatmap-economic-income.properties")
@PropertySource("classpath:configurations/bins/heatmap-economic-poverty.properties")
@PropertySource("classpath:configurations/bins/heatmap-region-type.properties")
@PropertySource("classpath:configurations/bins/heatmap-political-income.properties")
@ConfigurationProperties(prefix = "bins")
public class BinsConfig {

    Map<FeatureName, HashMap<Integer,Bin>> allBins;

    public Map<FeatureName, HashMap<Integer,Bin>> getAllBins() {
        return allBins;
    }

    public void setAllBins(Map<FeatureName, HashMap<Integer,Bin>> bins) {
        this.allBins = bins;
    }

    public HashMap<Integer, Bin> getFeatureBins(FeatureName feature) {
        return allBins.get(feature);
    }

    public void setFeatureBin(FeatureName feature, HashMap<Integer, Bin> bins) {
        allBins.put(feature, bins);
    }
}
