package me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class EIRacialRegionData {
    public EIRacialRegionData() {
        // No-args constructor
    }

    @Field("ALL")
    private EIRacialPlotData ALL;

    @Field("RURAL")
    private EIRacialPlotData RURAL;

    @Field("SUBURBAN")
    private EIRacialPlotData SUBURBAN;

    @Field("URBAN")
    private EIRacialPlotData URBAN;

    // Getters and Setters

    public EIRacialPlotData getALL() {
        return ALL;
    }

    public void setALL(EIRacialPlotData ALL) {
        this.ALL = ALL;
    }

    public EIRacialPlotData getRURAL() {
        return RURAL;
    }

    public void setRURAL(EIRacialPlotData RURAL) {
        this.RURAL = RURAL;
    }

    public EIRacialPlotData getSUBURBAN() {
        return SUBURBAN;
    }

    public void setSUBURBAN(EIRacialPlotData SUBURBAN) {
        this.SUBURBAN = SUBURBAN;
    }

    public EIRacialPlotData getURBAN() {
        return URBAN;
    }

    public void setURBAN(EIRacialPlotData URBAN) {
        this.URBAN = URBAN;
    }

}
