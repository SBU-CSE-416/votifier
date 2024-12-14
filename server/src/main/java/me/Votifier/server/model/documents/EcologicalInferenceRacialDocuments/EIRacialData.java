package me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
public class EIRacialData {
    public EIRacialData() {
        // No-args constructor
    }

    @Field("WHITE")
    private EIRacialPartyData WHITE;

    @Field("BLACK")
    private EIRacialPartyData BLACK;

    @Field("ASIAN")
    private EIRacialPartyData ASIAN;

    @Field("HISPANIC")
    private EIRacialPartyData HISPANIC;

    // Getters and Setters

    public EIRacialPartyData getWHITE() {
        return WHITE;
    }

    public void setWHITE(EIRacialPartyData WHITE) {
        this.WHITE = WHITE;
    }

    public EIRacialPartyData getBLACK() {
        return BLACK;
    }

    public void setBLACK(EIRacialPartyData BLACK) {
        this.BLACK = BLACK;
    }

    public EIRacialPartyData getASIAN() {
        return ASIAN;
    }

    public void setASIAN(EIRacialPartyData ASIAN) {
        this.ASIAN = ASIAN;
    }

    public EIRacialPartyData getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(EIRacialPartyData HISPANIC) {
        this.HISPANIC = HISPANIC;
    }
}
