package me.Votifier.server.model.configurations.charts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.HashMap;

import me.Votifier.server.model.configurations.FeatureName;

@Configuration
@PropertySource("classpath:configurations/charts/gingles-racial.properties")
@ConfigurationProperties(prefix = "charts")
public class ChartsConfig {

    Map<FeatureName, HashMap<Integer,Chart>> allCharts;

    public Map<FeatureName, HashMap<Integer,Chart>> getAllCharts() {
        return allCharts;
    }

    public void setAllCharts(Map<FeatureName, HashMap<Integer,Chart>> charts) {
        this.allCharts = charts;
    }

    public HashMap<Integer, Chart> getFeatureCharts(FeatureName feature) {
        return allCharts.get(feature);
    }

    public void setFeatureChart(FeatureName feature, HashMap<Integer, Chart> charts) {
        allCharts.put(feature, charts);
    }
}
