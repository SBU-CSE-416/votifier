package me.Votifier.server.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "state_summary")
public class StateSummary {

    @Id
    private String id;

    private String NAME;
    private double TOTAL_POPULATION;

    private double WHITE_PERCENT;
    private double BLACK_PERCENT;
    private double HISPANIC_PERCENT;
    private double ASIAN_PERCENT;
    private double OTHER_RACE_PERCENT;

    private double TOTAL_VOTES;
    private double DEM_VOTES;
    private double DEM_PERCENT;
    private double REP_VOTES;
    private double REP_PERCENT;

    private double TOTAL_HOUSEHOLDS;
    private Map<String, Double> HOUSEHOLD_INCOME_DISTRIBUTION;

    // Regional demographics
    private double RURAL_PERCENT;
    private double SUBURBAN_PERCENT;
    private double URBAN_PERCENT;

    private Map<String, EnsemblesStateSummary> ENSEMBLES;
    

    // Getters and setters

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

    public double getTOTAL_POPULATION() {
        return TOTAL_POPULATION;
    }

    public void setTOTAL_POPULATION(double TOTAL_POPULATION) {
        this.TOTAL_POPULATION = TOTAL_POPULATION;
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

    public double getHISPANIC_PERCENT() {
        return HISPANIC_PERCENT;
    }

    public void setHISPANIC_PERCENT(double HISPANIC_PERCENT) {
        this.HISPANIC_PERCENT = HISPANIC_PERCENT;
    }

    public double getASIAN_PERCENT() {
        return ASIAN_PERCENT;
    }

    public void setASIAN_PERCENT(double ASIAN_PERCENT) {
        this.ASIAN_PERCENT = ASIAN_PERCENT;
    }

    public double getOTHER_RACE_PERCENT() {
        return OTHER_RACE_PERCENT;
    }

    public void setOTHER_RACE_PERCENT(double OTHER_RACE_PERCENT) {
        this.OTHER_RACE_PERCENT = OTHER_RACE_PERCENT;
    }

    public double getTOTAL_VOTES() {
        return TOTAL_VOTES;
    }

    public void setTOTAL_VOTES(double TOTAL_VOTES) {
        this.TOTAL_VOTES = TOTAL_VOTES;
    }

    public double getDEM_VOTES() {
        return DEM_VOTES;
    }

    public void setDEM_VOTES(double DEM_VOTES) {
        this.DEM_VOTES = DEM_VOTES;
    }

    public double getDEM_PERCENT() {
        return DEM_PERCENT;
    }

    public void setDEM_PERCENT(double DEM_PERCENT) {
        this.DEM_PERCENT = DEM_PERCENT;
    }

    public double getREP_VOTES() {
        return REP_VOTES;
    }

    public void setREP_VOTES(double REP_VOTES) {
        this.REP_VOTES = REP_VOTES;
    }

    public double getREP_PERCENT() {
        return REP_PERCENT;
    }

    public void setREP_PERCENT(double REP_PERCENT) {
        this.REP_PERCENT = REP_PERCENT;
    }

    public double getTOTAL_HOUSEHOLDS() {
        return TOTAL_HOUSEHOLDS;
    }

    public void setTOTAL_HOUSEHOLDS(double TOTAL_HOUSEHOLDS) {
        this.TOTAL_HOUSEHOLDS = TOTAL_HOUSEHOLDS;
    }

    public Map<String, Double> getHOUSE_HOLD_INCOME_DISTRIBUTION() {
        return HOUSEHOLD_INCOME_DISTRIBUTION;
    }

    public void setHOUSE_HOLD_INCOME_DISTRIBUTION(Map<String, Double> HOUSE_HOLD_INCOME_DISTRIBUTION) {
        this.HOUSEHOLD_INCOME_DISTRIBUTION = HOUSE_HOLD_INCOME_DISTRIBUTION;
    }

    public double getRURAL_PERCENT() {
        return RURAL_PERCENT;
    }

    public void setRURAL_PERCENT(double RURAL_PERCENT) {
        this.RURAL_PERCENT = RURAL_PERCENT;
    }

    public double getSUBURBAN_PERCENT() {
        return SUBURBAN_PERCENT;
    }

    public void setSUBURBAN_PERCENT(double SUBURBAN_PERCENT) {
        this.SUBURBAN_PERCENT = SUBURBAN_PERCENT;
    }

    public double getURBAN_PERCENT() {
        return URBAN_PERCENT;
    }

    public void setURBAN_PERCENT(double URBAN_PERCENT) {
        this.URBAN_PERCENT = URBAN_PERCENT;
    }

    public Map<String, EnsemblesStateSummary> getENSEMBLES() {
        return ENSEMBLES;
    }

    public void setENSEMBLES(Map<String, EnsemblesStateSummary> ENSEMBLES) {
        this.ENSEMBLES = ENSEMBLES;
    }
}

