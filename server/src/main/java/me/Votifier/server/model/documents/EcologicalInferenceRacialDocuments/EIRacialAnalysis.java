package me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "racial_ecological_inference_data")
public class EIRacialAnalysis {
    public EIRacialAnalysis() {
        // No-args constructor
    }
    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("election")
    private String election;

    @Field("data")
    private EIRacialData data;

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

    public EIRacialData getData() {
        return data;
    }

    public void setData(EIRacialData data) {
        this.data = data;
    }
    
}
