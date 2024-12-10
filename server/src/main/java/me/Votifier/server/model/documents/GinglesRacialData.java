package me.Votifier.server.model.documents;

import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

public class GinglesRacialData {
    public GinglesRacialData() {
        // No-args constructor
    }
    @Field("WHITE")
    private List<GinglesRacialPrecinctData> WHITE;

    @Field("BLACK")
    private List<GinglesRacialPrecinctData> BLACK;

    @Field("ASIAN")
    private List<GinglesRacialPrecinctData> ASIAN;

    @Field("HISPANIC")
    private List<GinglesRacialPrecinctData> HISPANIC;

    // Getters and Setters
    public List<GinglesRacialPrecinctData> getWHITE() {
        return WHITE;
    }

    public void setWHITE(List<GinglesRacialPrecinctData> WHITE) {
        this.WHITE = WHITE;
    }

    public List<GinglesRacialPrecinctData> getBLACK() {
        return BLACK;
    }

    public void setBLACK(List<GinglesRacialPrecinctData> BLACK) {
        this.BLACK = BLACK;
    }

    public List<GinglesRacialPrecinctData> getASIAN() {
        return ASIAN;
    }

    public void setASIAN(List<GinglesRacialPrecinctData> ASIAN) {
        this.ASIAN = ASIAN;
    }

    public List<GinglesRacialPrecinctData> getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(List<GinglesRacialPrecinctData> HISPANIC) {
        this.HISPANIC = HISPANIC;
    }
}
