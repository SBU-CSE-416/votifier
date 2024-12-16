package me.Votifier.server.model.documents.BoxplotIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;
public class BoxplotIncomePlotData {
    public BoxplotIncomePlotData() {
        // No-args constructor
    }

    @Field("labels")
    private BoxPlotLabel labels;

    @Field("data")
    private Map<String, BoxPlotStats> data;

    // Getters and Setters

    public BoxPlotLabel getLabels() {
        return labels;
    }

    public void setLabels(BoxPlotLabel labels) {
        this.labels = labels;
    }

    public Map<String, BoxPlotStats> getData() {
        return data;
    }

    public void setData(Map<String, BoxPlotStats> data) {
        this.data = data;
    }
}
