package me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class EIRacialPlotData {
    public EIRacialPlotData() {
        // No-args constructor
    }

    @Field("data")
    RacialSampleData data;

    @Field("group_names")
    List<String> group_names;

    @Field("candidate_names")
    String candidate_names;

    //getter and setter

    public RacialSampleData getData() {
        return data;
    }

    public void setData(RacialSampleData data) {
        this.data = data;
    }

    public List<String> getGroup_names() {
        return group_names;
    }

    public void setGroup_names(List<String> group_names) {
        this.group_names = group_names;
    }

    public String getCandidate_names() {
        return candidate_names;
    }

    public void setCandidate_names(String candidate_names) {
        this.candidate_names = candidate_names;
    }

    public static class RacialSampleData {
        @Field("sampled_voting_prefs")
        List<List<Double>> sampled_voting_prefs;

        //getter and setter

        public List<List<Double>> getSampled_voting_prefs() {
            return sampled_voting_prefs;
        }

        public void setSampled_voting_prefs(List<List<Double>> sampled_voting_prefs) {
            this.sampled_voting_prefs = sampled_voting_prefs;
        }
    }

}
