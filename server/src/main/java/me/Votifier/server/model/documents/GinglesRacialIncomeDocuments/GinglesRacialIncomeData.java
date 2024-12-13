package me.Votifier.server.model.documents.GinglesRacialIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class GinglesRacialIncomeData {
    public GinglesRacialIncomeData() {
        // No-args constructor
    }
    @Field("WHITE")
    private List<GinglesRacialIncomePrecinctData> WHITE;

    @Field("BLACK")
    private List<GinglesRacialIncomePrecinctData> BLACK;

    @Field("ASIAN")
    private List<GinglesRacialIncomePrecinctData> ASIAN;

    @Field("HISPANIC")
    private List<GinglesRacialIncomePrecinctData> HISPANIC;

    // Getters and Setters
    public List<GinglesRacialIncomePrecinctData> getWHITE() {
        return WHITE;
    }

    public void setWHITE(List<GinglesRacialIncomePrecinctData> WHITE) {
        this.WHITE = WHITE;
    }

    public List<GinglesRacialIncomePrecinctData> getBLACK() {
        return BLACK;
    }

    public void setBLACK(List<GinglesRacialIncomePrecinctData> BLACK) {
        this.BLACK = BLACK;
    }

    public List<GinglesRacialIncomePrecinctData> getASIAN() {
        return ASIAN;
    }

    public void setASIAN(List<GinglesRacialIncomePrecinctData> ASIAN) {
        this.ASIAN = ASIAN;
    }

    public List<GinglesRacialIncomePrecinctData> getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(List<GinglesRacialIncomePrecinctData> HISPANIC) {
        this.HISPANIC = HISPANIC;
    }
}
