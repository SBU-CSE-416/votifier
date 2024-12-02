package me.Votifier.server.model.configurations.charts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

import me.Votifier.server.model.configurations.FeatureName;

@Configuration
@PropertySource("classpath:configurations/charts/gingles-demographic.properties")
@ConfigurationProperties(prefix = "charts")
public class ChartsConfig {

    Map<FeatureName, Chart> allCharts;

    public Map<FeatureName, Chart> getAllCharts() {
        return allCharts;
    }

    public void setAllCharts(Map<FeatureName, Chart> charts) {
        this.allCharts = charts;
    }

    public Chart getFeatureChart(FeatureName feature) {
        return allCharts.get(feature);
    }

    public void setFeatureChart(FeatureName feature, Chart chart) {
        allCharts.put(feature, chart);
    }
}
