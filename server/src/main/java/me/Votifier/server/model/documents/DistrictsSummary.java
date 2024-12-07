package me.Votifier.server.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "districts_summary")
public class DistrictsSummary {

    @Id
    private String id;

    @Field("NAME")
    private String name;

    @Field("election")
    private String election;

    @Field("data")
    private List<DistrictData> data;


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public List<DistrictData> getData() {
        return data;
    }

    public void setData(List<DistrictData> data) {
        this.data = data;
    }
    
    public class DistrictData {

        @Field("CONG_DIST")
        private int CONG_DIST;
    
        @Field("REPRESENTATIVE")
        private String REPRESENTATIVE;
    
        @Field("PARTY")
        private String PARTY;
    
        @Field("RACE")
        private String RACE;
    
        @Field("AVERAGE_HOUSEHOLD_INCOME")
        private int AVERAGE_HOUSEHOLD_INCOME;
    
        @Field("PERCENT_BELOW_POVERTY")
        private int PERCENT_BELOW_POVERTY;
    
        @Field("RURAL_PERCENT")
        private int RURAL_PERCENT;
    
        @Field("SUBURBAN_PERCENT")
        private int SUBURBAN_PERCENT;
    
        @Field("URBAN_PERCENT")
        private int URBAN_PERCENT;
    
        @Field("REP_PERCENT")
        private int REP_PERCENT;
    
        @Field("DEM_PERCENT")
        private int DEM_PERCENT;
    
        // Getters and Setters
        public int getCONG_DIST() {
            return CONG_DIST;
        }
    
        public void setCONG_DIST(int CONG_DIST) {
            this.CONG_DIST = CONG_DIST;
        }
    
        public String getREPRESENTATIVE() {
            return REPRESENTATIVE;
        }
    
        public void setREPRESENTATIVE(String REPRESENTATIVE) {
            this.REPRESENTATIVE = REPRESENTATIVE;
        }
    
        public String getPARTY() {
            return PARTY;
        }
    
        public void setPARTY(String PARTY) {
            this.PARTY = PARTY;
        }
    
        public String getRACE() {
            return RACE;
        }
    
        public void setRACE(String RACE) {
            this.RACE = RACE;
        }
    
        public int getAVERAGE_HOUSEHOLD_INCOME() {
            return AVERAGE_HOUSEHOLD_INCOME;
        }
    
        public void setAVERAGE_HOUSEHOLD_INCOME(int AVERAGE_HOUSEHOLD_INCOME) {
            this.AVERAGE_HOUSEHOLD_INCOME = AVERAGE_HOUSEHOLD_INCOME;
        }
    
        public int getPERCENT_BELOW_POVERTY() {
            return PERCENT_BELOW_POVERTY;
        }
    
        public void setPERCENT_BELOW_POVERTY(int PERCENT_BELOW_POVERTY) {
            this.PERCENT_BELOW_POVERTY = PERCENT_BELOW_POVERTY;
        }
    
        public int getRURAL_PERCENT() {
            return RURAL_PERCENT;
        }
    
        public void setRURAL_PERCENT(int RURAL_PERCENT) {
            this.RURAL_PERCENT = RURAL_PERCENT;
        }
    
        public int getSUBURBAN_PERCENT() {
            return SUBURBAN_PERCENT;
        }
    
        public void setSUBURBAN_PERCENT(int SUBURBAN_PERCENT) {
            this.SUBURBAN_PERCENT = SUBURBAN_PERCENT;
        }
    
        public int getURBAN_PERCENT() {
            return URBAN_PERCENT;
        }
    
        public void setURBAN_PERCENT(int URBAN_PERCENT) {
            this.URBAN_PERCENT = URBAN_PERCENT;
        }
    
        public int getREP_PERCENT() {
            return REP_PERCENT;
        }
    
        public void setREP_PERCENT(int REP_PERCENT) {
            this.REP_PERCENT = REP_PERCENT;
        }
    
        public int getDEM_PERCENT() {
            return DEM_PERCENT;
        }
    
        public void setDEM_PERCENT(int DEM_PERCENT) {
            this.DEM_PERCENT = DEM_PERCENT;
        }
    }
}

