package me.Votifier.server.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "state_summary")
public class StateSummary {

    @Id
    private String id; 

    private String NAME; 
    private double TOT_POP; 
    private double TOT_WHITE; 
    private double TOT_BLACK;
    private double TOT_HISP; 
    private double TOT_ASIAN;
    private double TOT_NATIVE; 
    private double TOT_ISLANDER; 
    private double TOT_OTHER; 

    private double DEM_VOT_DIS; 
    private double REP_VOT_DIS; 

    private double URBAN_DIS;
    private double SUBURBAN_DIS; 
    private double RURAL_DIS; 

    private double TOT_HOUS; 

    private Map<String, Double> HOUS_INCOME_DIS; 
    private double POV_LEVEL; 

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

    public double getTOT_POP() {
        return TOT_POP;
    }

    public void setTOT_POP(double TOT_POP) {
        this.TOT_POP = TOT_POP;
    }

    public double getTOT_WHITE() {
        return TOT_WHITE;
    }

    public void setTOT_WHITE(double TOT_WHITE) {
        this.TOT_WHITE = TOT_WHITE;
    }

    public double getTOT_BLACK() {
        return TOT_BLACK;
    }

    public void setTOT_BLACK(double TOT_BLACK) {
        this.TOT_BLACK = TOT_BLACK;
    }

    public double getTOT_HISP() {
        return TOT_HISP;
    }

    public void setTOT_HISP(double TOT_HISP) {
        this.TOT_HISP = TOT_HISP;
    }

    public double getTOT_ASIAN() {
        return TOT_ASIAN;
    }

    public void setTOT_ASIAN(double TOT_ASIAN) {
        this.TOT_ASIAN = TOT_ASIAN;
    }

    public double getTOT_NATIVE() {
        return TOT_NATIVE;
    }

    public void setTOT_NATIVE(double TOT_NATIVE) {
        this.TOT_NATIVE = TOT_NATIVE;
    }

    public double getTOT_ISLANDER() {
        return TOT_ISLANDER;
    }

    public void setTOT_ISLANDER(double TOT_ISLANDER) {
        this.TOT_ISLANDER = TOT_ISLANDER;
    }

    public double getTOT_OTHER() {
        return TOT_OTHER;
    }

    public void setTOT_OTHER(double TOT_OTHER) {
        this.TOT_OTHER = TOT_OTHER;
    }

    public double getDEM_VOT_DIS() {
        return DEM_VOT_DIS;
    }

    public void setDEM_VOT_DIS(double DEM_VOT_DIS) {
        this.DEM_VOT_DIS = DEM_VOT_DIS;
    }

    public double getREP_VOT_DIS() {
        return REP_VOT_DIS;
    }

    public void setREP_VOT_DIS(double REP_VOT_DIS) {
        this.REP_VOT_DIS = REP_VOT_DIS;
    }

    public double getURBAN_DIS() {
        return URBAN_DIS;
    }

    public void setURBAN_DIS(double URBAN_DIS) {
        this.URBAN_DIS = URBAN_DIS;
    }

    public double getSUBURBAN_DIS() {
        return SUBURBAN_DIS;
    }

    public void setSUBURBAN_DIS(double SUBURBAN_DIS) {
        this.SUBURBAN_DIS = SUBURBAN_DIS;
    }

    public double getRURAL_DIS() {
        return RURAL_DIS;
    }

    public void setRURAL_DIS(double RURAL_DIS) {
        this.RURAL_DIS = RURAL_DIS;
    }

    public double getTOT_HOUS() {
        return TOT_HOUS;
    }

    public void setTOT_HOUS(double TOT_HOUS) {
        this.TOT_HOUS = TOT_HOUS;
    }

    public Map<String, Double> getHOUS_INCOME_DIS() {
        return HOUS_INCOME_DIS;
    }

    public void setHOUS_INCOME_DIS(Map<String, Double> HOUS_INCOME_DIS) {
        this.HOUS_INCOME_DIS = HOUS_INCOME_DIS;
    }

    public double getPOV_LEVEL() {
        return POV_LEVEL;
    }

    public void setPOV_LEVEL(double POV_LEVEL) {
        this.POV_LEVEL = POV_LEVEL;
    }
}