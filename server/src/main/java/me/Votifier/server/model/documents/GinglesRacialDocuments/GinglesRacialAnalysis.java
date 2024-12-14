package me.Votifier.server.model.documents.GinglesRacialDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "gingles_racial_data")
public class GinglesRacialAnalysis {
    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("election")
    private String election;
    
    @Field("candidates")
    private Map<String, String> candidates;
    

    @Field("data")
    private GinglesRacialData data;

    @Field("lines")
    private GinglesRacialLineData lines;
    

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

    public GinglesRacialData getData() {
        return data;
    }

    public void setData(GinglesRacialData data) {
        this.data = data;
    }

    public Map<String, String> getCandidates() {
        return candidates;
    }

    public void setCandidates(Map<String, String> candidates) {
        this.candidates = candidates;
    }
    public GinglesRacialLineData getLines() {
        return lines;
    }

    public void setLines(GinglesRacialLineData lines) {
        this.lines = lines;
    }

    
}
