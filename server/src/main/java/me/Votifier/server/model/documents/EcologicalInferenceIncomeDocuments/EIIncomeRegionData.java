package me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments;
import org.springframework.data.mongodb.core.mapping.Field;
public class EIIncomeRegionData {

    public EIIncomeRegionData() {
        // No-args constructor
    }

    @Field("ALL")

    private EIIncomePlotData ALL;

    @Field("RURAL")

    private EIIncomePlotData RURAL;

    @Field("SUBURBAN")

    private EIIncomePlotData SUBURBAN;

    @Field("URBAN")

    private EIIncomePlotData URBAN;

    // Getters and Setters

    public EIIncomePlotData getALL() {
        return ALL;
    }

    public void setALL(EIIncomePlotData ALL) {
        this.ALL = ALL;
    }

    public EIIncomePlotData getRURAL() {
        return RURAL;
    }

    public void setRURAL(EIIncomePlotData RURAL) {
        this.RURAL = RURAL;
    }

    public EIIncomePlotData getSUBURBAN() {
        return SUBURBAN;
    }

    public void setSUBURBAN(EIIncomePlotData SUBURBAN) {
        this.SUBURBAN = SUBURBAN;
    }

    public EIIncomePlotData getURBAN() {
        return URBAN;
    }

    public void setURBAN(EIIncomePlotData URBAN) {
        this.URBAN = URBAN;
    }

    
}
