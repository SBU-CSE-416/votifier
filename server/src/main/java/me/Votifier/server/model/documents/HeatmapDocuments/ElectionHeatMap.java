
package me.Votifier.server.model.documents.HeatmapDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "election_data")
public class ElectionHeatMap {
    public ElectionHeatMap() {
        // No-args constructor
    }
    @Field("NAME")
    private String NAME;

    @Field("election")
    private String election;
    @Field("data")
    private List<ElectionHeatMapData> data;

    //getter and setter

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

    public List<ElectionHeatMapData> getData() {
        return data;
    }

    public void setData(List<ElectionHeatMapData> data) {
        this.data = data;
    }

    public static class ElectionHeatMapData {
        public ElectionHeatMapData() {
            // No-args constructor
        }
        @Field("UNIQUE_ID")
        private String UNIQUE_ID;

        @Field("TOT_REP")
        private String TOT_REP;

        @Field("TOT_DEM")

        private String TOT_DEM;

        @Field("TOT_VOT")   
        private String TOT_VOT;

        @Field("LEAN")
        private String LEAN;

        // Getters and Setters

        public String getUNIQUE_ID() {
            return UNIQUE_ID;
        }

        public void setUNIQUE_ID(String UNIQUE_ID) {
            this.UNIQUE_ID = UNIQUE_ID;
        }

        public String getTOT_REP() {
            return TOT_REP;
        }

        public void setTOT_REP(String TOT_REP) {
            this.TOT_REP = TOT_REP;
        }

        public String getTOT_DEM() {
            return TOT_DEM;
        }

        public void setTOT_DEM(String TOT_DEM) {
            this.TOT_DEM = TOT_DEM;
        }

        public String getTOT_VOT() {
            return TOT_VOT;
        }

        public void setTOT_VOT(String TOT_VOT) {
            this.TOT_VOT = TOT_VOT;
        }

        public String getLEAN() {
            return LEAN;
        }

        public void setLEAN(String LEAN) {
            this.LEAN = LEAN;
        }
    }
}