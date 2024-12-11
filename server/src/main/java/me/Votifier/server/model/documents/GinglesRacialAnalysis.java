package me.Votifier.server.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Map;
import java.util.List;

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
    private Lines lines;
    

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
    public Lines getLines() {
        return lines;
    }

    public void setLines(Lines lines) {
        this.lines = lines;
    }

    public static class Lines {
        @Field("democratic")
        private LineData democratic;

        @Field("republican")
        private LineData republican;

        // Getters and Setters
        public LineData getDemocratic() {
            return democratic;
        }

        public void setDemocratic(LineData democratic) {
            this.democratic = democratic;
        }

        public LineData getRepublican() {
            return republican;
        }

        public void setRepublican(LineData republican) {
            this.republican = republican;
        }
    }
    public static class LineData {
        @Field("x")
        private List<Double> x;

        @Field("y")
        private List<Double> y;

        // Getters and Setters
        public List<Double> getX() {
            return x;
        }

        public void setX(List<Double> x) {
            this.x = x;
        }

        public List<Double> getY() {
            return y;
        }

        public void setY(List<Double> y) {
            this.y = y;
        }
    }
}
