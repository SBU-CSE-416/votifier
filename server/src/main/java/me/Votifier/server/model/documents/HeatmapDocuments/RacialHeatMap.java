
package me.Votifier.server.model.documents.HeatmapDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "demographic_data")
public class RacialHeatMap {
    public RacialHeatMap() {
        // No-args constructor
    }
    @Field("NAME")
    private String NAME;

    @Field("data")
    private List<RacialHeatMapData> data;

    //getter and setter

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<RacialHeatMapData> getData() {
        return data;
    }

    public void setData(List<RacialHeatMapData> data) {
        this.data = data;
    }

    public static class RacialHeatMapData {
        public RacialHeatMapData() {
            // No-args constructor
        }
        @Field("UNIQUE_ID")
        private String UNIQUE_ID;

        @Field("TOT_POP22")
        private String TOTAL_POPULATION;

        @Field("WHT_NHSP22")
        private String WHITE;

        @Field("BLK_NHSP22")

        private String BLACK;

        @Field("ASN_NHSP22")

        private String ASIAN;

        @Field("HSP_POP22")

        private String HISPANIC;

        // Getters and Setters

        public String getUNIQUE_ID() {
            return UNIQUE_ID;
        }

        public void setUNIQUE_ID(String UNIQUE_ID) {
            this.UNIQUE_ID = UNIQUE_ID;
        }

        public String getWHITE() {
            return WHITE;
        }

        public void setWHITE(String WHITE) {
            this.WHITE = WHITE;
        }

        public String getBLACK() {
            return BLACK;
        }

        public void setBLACK(String BLACK) {
            this.BLACK = BLACK;
        }

        public String getASIAN() {
            return ASIAN;
        }

        public void setASIAN(String ASIAN) {
            this.ASIAN = ASIAN;
        }

        public String getHISPANIC() {
            return HISPANIC;
        }

        public void setHISPANIC(String HISPANIC) {
            this.HISPANIC = HISPANIC;
        }

        public String getTOTAL_POPULATION() {
            return TOTAL_POPULATION;
        }

        public void setTOTAL_POPULATION(String TOTAL_POPULATION) {
            this.TOTAL_POPULATION = TOTAL_POPULATION;
        }
    }
}