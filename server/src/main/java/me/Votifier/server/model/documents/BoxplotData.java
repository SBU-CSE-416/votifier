package me.Votifier.server.model.documents;

import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

public class GinglesData {
    public GinglesData() {
        // No-args constructor
    }
    @Field("WHITE")
    private List<GinglePrecinctData> WHITE;

    @Field("BLACK")
    private List<GinglePrecinctData> BLACK;

    @Field("ASIAN")
    private List<GinglePrecinctData> ASIAN;

    @Field("HISPANIC")
    private List<GinglePrecinctData> HISPANIC;

    // Getters and Setters
    public List<GinglePrecinctData> getWHITE() {
        return WHITE;
    }

    public void setWHITE(List<GinglePrecinctData> WHITE) {
        this.WHITE = WHITE;
    }

    public List<GinglePrecinctData> getBLACK() {
        return BLACK;
    }

    public void setBLACK(List<GinglePrecinctData> BLACK) {
        this.BLACK = BLACK;
    }

    public List<GinglePrecinctData> getASIAN() {
        return ASIAN;
    }

    public void setASIAN(List<GinglePrecinctData> ASIAN) {
        this.ASIAN = ASIAN;
    }

    public List<GinglePrecinctData> getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(List<GinglePrecinctData> HISPANIC) {
        this.HISPANIC = HISPANIC;
    }
}
