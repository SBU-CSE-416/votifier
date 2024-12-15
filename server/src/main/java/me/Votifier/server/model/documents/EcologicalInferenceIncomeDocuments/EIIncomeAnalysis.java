package me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "economic_ecological_inference_data")
public class EIIncomeAnalysis {
    public EIIncomeAnalysis() {
        // No-args constructor
    }
    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("election")
    private String election;

    @Field("data")
    private EIIncomeData data;

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
    
    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public EIIncomeData getData() {
        return data;
    }

    public void setData(EIIncomeData data) {
        this.data = data;
    }
    
}

