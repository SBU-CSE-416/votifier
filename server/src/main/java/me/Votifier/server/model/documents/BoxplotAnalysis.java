package me.Votifier.server.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Map;

@Document(collection = "boxplot_racial_data")
public class BoxplotAnalysis {
    // @Id
    // private String id;

    // @Field("NAME")
    // private String NAME;

    // @Field("election")
    // private String election;
    
    // @Field("candidates")
    // private Map<String, String> candidates;
    

    @Field("data")
    private BoxplotData data;

    //getters and setters
    // public String getId() {
    //     return id;
    // }

    // public void setId(String id) {
    //     this.id = id;
    // }

    // public String getNAME() {
    //     return NAME;
    // }

    // public void setNAME(String NAME) {
    //     this.NAME = NAME;
    // }

    // public String getElection() {
    //     return election;
    // }

    // public void setElection(String election) {
    //     this.election = election;
    // }

    public BoxplotData getData() {
        return data;
    }

    public void setData(BoxplotData data) {
        this.data = data;
    }

    // public Map<String, String> getCandidates() {
    //     return candidates;
    // }

    // public void setCandidates(Map<String, String> candidates) {
    //     this.candidates = candidates;
    // }
    
}
