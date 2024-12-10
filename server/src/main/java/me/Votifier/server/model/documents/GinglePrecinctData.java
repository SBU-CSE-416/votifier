package me.Votifier.server.model.documents;

import org.springframework.data.mongodb.core.mapping.Field;

public class GinglePrecinctData {
    public GinglePrecinctData() {
        // No-args constructor
    }
    
    @Field("UNIQUE_ID")
    private String UNIQUE_ID;

    @Field("REPUBLICAN_VOTE_SHARE")
    private double REPUBLICAN_VOTE_SHARE;

    @Field("DEMOCRATIC_VOTE_SHARE")
    private double DEMOCRATIC_VOTE_SHARE;

    @Field("WHITE_PERCENT")
    private double WHITE_PERCENT;

    @Field("BLACK_PERCENT")
    private double BLACK_PERCENT;

    @Field("ASIAN_PERCENT")
    private double ASIAN_PERCENT;

    @Field("HISPANIC_PERCENT")
    private double HISPANIC_PERCENT;

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

    public double getWHITE_PERCENT() {
        return WHITE_PERCENT;
    }

    public void setWHITE_PERCENT(double WHITE_PERCENT) {
        this.WHITE_PERCENT = WHITE_PERCENT;
    }

    public double getBLACK_PERCENT() {
        return BLACK_PERCENT;
    }

    public void setBLACK_PERCENT(double BLACK_PERCENT) {
        this.BLACK_PERCENT = BLACK_PERCENT;
    }

    public double getASIAN_PERCENT() {
        return ASIAN_PERCENT;
    }

    public void setASIAN_PERCENT(double ASIAN_PERCENT) {
        this.ASIAN_PERCENT = ASIAN_PERCENT;
    }

    public double getHISPANIC_PERCENT() {
        return HISPANIC_PERCENT;
    }

    public void setHISPANIC_PERCENT(double HISPANIC_PERCENT) {
        this.HISPANIC_PERCENT = HISPANIC_PERCENT;
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
