package me.Votifier.server.model.documents.PlanSplitsDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "plan_splits_data")
public class PlanSplitsAnalysis {
    public PlanSplitsAnalysis() {
        // No-args constructor
    }

    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("data")
    private PlanSplitsData data;

    //getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public PlanSplitsData getData() {
        return data;
    }

    public void setData(PlanSplitsData data) {
        this.data = data;
    }

    
}