package me.Votifier.server.model.documents.GinglesIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class GinglesIncomePrecinctData {
    public GinglesIncomePrecinctData() {
        // No-args constructor
    }
    @Field("UNIQUE_ID")
    private String UNIQUE_ID;

    @Field("REPUBLICAN_VOTE_SHARE")
    private double REPUBLICAN_VOTE_SHARE;

    @Field("DEMOCRATIC_VOTE_SHARE")
    private double DEMOCRATIC_VOTE_SHARE;

    @Field("AVG_HOUSEHOLD_INCOME")
    private double AVG_HOUSEHOLD_INCOME;

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

    public double getAVG_HOUSEHOLD_INCOME() {
        return AVG_HOUSEHOLD_INCOME;
    }

    public void setAVG_HOUSEHOLD_INCOME(double AVG_HOUSEHOLD_INCOME) {
        this.AVG_HOUSEHOLD_INCOME = AVG_HOUSEHOLD_INCOME;
    }
}