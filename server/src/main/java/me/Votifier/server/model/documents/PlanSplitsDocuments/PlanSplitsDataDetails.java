package me.Votifier.server.model.documents.PlanSplitsDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

public class PlanSplitsDataDetails {
    public PlanSplitsDataDetails() {
        // No-args constructor
    }

    @Field("labels")
    private PlanSplitsLabel labels;

    @Field("data")
    private Map<String, Integer> data; 

    // Getters and Setters

    public PlanSplitsLabel getLabels() {
        return labels;
    }

    public void setLabels(PlanSplitsLabel labels) {
        this.labels = labels;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }
}
