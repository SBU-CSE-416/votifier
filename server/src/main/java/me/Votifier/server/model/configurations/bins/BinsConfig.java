package me.Votifier.server.model.configurations.bins;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import me.Votifier.server.model.Bin;
import me.Votifier.server.model.BinsFeature;

import java.util.Map;
import java.util.TreeMap;

@Configuration
@PropertySource("classpath:configurations/bins/heatmap-demographic.properties")
@ConfigurationProperties(prefix = "bins")
public class BinsConfig {

    Map<BinsFeature, TreeMap<Integer,Bin>> allBins;

    public Map<BinsFeature, TreeMap<Integer,Bin>> getAllBins() {
        return allBins;
    }

    public void setAllBins(Map<BinsFeature, TreeMap<Integer,Bin>> bins) {
        this.allBins = bins;
    }

    public TreeMap<Integer, Bin> getFeatureBins(BinsFeature feature) {
        return allBins.get(feature);
    }

    public void setFeatureBin(BinsFeature feature, TreeMap<Integer, Bin> bins) {
        allBins.put(feature, bins);
    }
}
