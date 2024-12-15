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

    
}
