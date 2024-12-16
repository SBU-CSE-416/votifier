package me.Votifier.server.model.documents.BoxplotIncomeDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "economic_boxplots_data")
public class BoxplotIncomeAnalysis {
    public BoxplotIncomeAnalysis() {
        // No-args constructor
    }

    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("data")
    private BoxplotIncomeData data;

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

    public BoxplotIncomeData getData() {
        return data;
    }

    public void setData(BoxplotIncomeData data) {
        this.data = data;
    }

    
}

