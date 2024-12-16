package me.Votifier.server.model.documents.BoxplotRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
public class BoxplotRacialEnsembleRace {
    public BoxplotRacialEnsembleRace() {
        // No-args constructor
    }

    @Field("WHITE")

    private BoxplotRacialPlotData WHITE;

    @Field("BLACK")

    private BoxplotRacialPlotData BLACK;

    @Field("ASIAN")

    private BoxplotRacialPlotData ASIAN;

    @Field("HISPANIC")
        
    private BoxplotRacialPlotData HISPANIC;
    // Getters and Setters

    public BoxplotRacialPlotData getWHITE() {
        return WHITE;
    }

    public void setWHITE(BoxplotRacialPlotData WHITE) {
        this.WHITE = WHITE;
    }

    public BoxplotRacialPlotData getBLACK() {
        return BLACK;
    }

    public void setBLACK(BoxplotRacialPlotData BLACK) {
        this.BLACK = BLACK;
    }

    public BoxplotRacialPlotData getASIAN() {
        return ASIAN;
    }

    public void setASIAN(BoxplotRacialPlotData ASIAN) {
        this.ASIAN = ASIAN;
    }

    public BoxplotRacialPlotData getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(BoxplotRacialPlotData HISPANIC) {
        this.HISPANIC = HISPANIC;
    }
    
}
