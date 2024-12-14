
package me.Votifier.server.model.documents.GinglesRacialIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class GinglesRacialIncomePrecinctData {
    public GinglesRacialIncomePrecinctData() {
        // No-args constructor
    }
    
    @Field("UNIQUE_ID")
    private String UNIQUE_ID;

    @Field("REPUBLICAN_VOTE_SHARE")
    private double REPUBLICAN_VOTE_SHARE;

    @Field("DEMOCRATIC_VOTE_SHARE")
    private double DEMOCRATIC_VOTE_SHARE;

    @Field("RACE_INCOME_PERCENT")
    private double RACE_INCOME_PERCENT;

    @Field("AVG_HOUSEHOLD_INCOME")
    private double AVG_HOUSEHOLD_INCOME;

    @Field("REGION_TYPE")
    private String REGION_TYPE;

    //getters and setters
    public String getUNIQUE_ID() {
        return UNIQUE_ID;
    }

    public void setUNIQUE_ID(String UNIQUE_ID) {
        this.UNIQUE_ID = UNIQUE_ID;
    }

    public double getREPUBLICAN_VOTE_SHARE() {
        return REPUBLICAN_VOTE_SHARE;
    }

    public void setREPUBLICAN_VOTE_SHARE(double REPUBLICAN_VOTE_SHARE) {
        this.REPUBLICAN_VOTE_SHARE = REPUBLICAN_VOTE_SHARE;
    }

    public double getDEMOCRATIC_VOTE_SHARE() {
        return DEMOCRATIC_VOTE_SHARE;
    }

    public void setDEMOCRATIC_VOTE_SHARE(double DEMOCRATIC_VOTE_SHARE) {
        this.DEMOCRATIC_VOTE_SHARE = DEMOCRATIC_VOTE_SHARE;
    }

    public double getRACE_INCOME_PERCENT() {
        return this.RACE_INCOME_PERCENT;
    }

    public void setRACE_INCOME_PERCENT(double RACE_INCOME_PERCENT) {
        this.RACE_INCOME_PERCENT = RACE_INCOME_PERCENT;
    }
    public double getAVG_HOUSEHOLD_INCOME() {
        return AVG_HOUSEHOLD_INCOME;
    }

    public void setAVG_HOUSEHOLD_INCOME(double AVG_HOUSEHOLD_INCOME) {
        this.AVG_HOUSEHOLD_INCOME = AVG_HOUSEHOLD_INCOME;
    }

    public String getREGION_TYPE() {
        return REGION_TYPE;
    }


    public void setREGION_TYPE(String REGION_TYPE) {
        this.REGION_TYPE = REGION_TYPE;
    }
}
