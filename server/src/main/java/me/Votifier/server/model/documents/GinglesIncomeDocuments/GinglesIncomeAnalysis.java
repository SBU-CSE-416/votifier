package me.Votifier.server.model.documents.GinglesIncomeDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "gingles_economic_data")
public class GinglesIncomeAnalysis {
    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("election")
    private String election;
    
    @Field("candidates")
    private Map<String, String> candidates;

    @Field("data")
    private GinglesIncomeData data;

    @Field("lines")
    private GinglesIncomeLineData lines;
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

    public GinglesIncomeData getData() {
        return data;
    }

    public void setData(GinglesIncomeData data) {
        this.data = data;
    }
    public GinglesIncomeLineData getLines() {
        return lines;
    }

    public void setLines(GinglesIncomeLineData lines) {
        this.lines = lines;
    }

    public Map<String, String> getCandidates() {
        return candidates;
    }

    public void setCandidates(Map<String, String> candidates) {
        this.candidates = candidates;
    }
    

}
