package me.Votifier.server.model.documents.GinglesIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class GinglesIncomeData {
    public GinglesIncomeData() {
        // No-args constructor
    }
    @Field("ALL")
    private List<GinglesIncomePrecinctData> ALL;

    @Field("RURAL")
    private List<GinglesIncomePrecinctData> RURAL;

    @Field("SUBURBAN")
    private List<GinglesIncomePrecinctData> SUBURBAN;

    @Field("URBAN")
    private List<GinglesIncomePrecinctData> URBAN;

    //getter and setter

    public List<GinglesIncomePrecinctData> getALL() {
        return ALL;
    }

    public void setALL(List<GinglesIncomePrecinctData> ALL) {
        this.ALL = ALL;
    }

    public List<GinglesIncomePrecinctData> getRURAL() {
        return RURAL;
    }

    public void setRURAL(List<GinglesIncomePrecinctData> RURAL) {
        this.RURAL = RURAL;
    }

    public List<GinglesIncomePrecinctData> getSUBURBAN() {
        return SUBURBAN;
    }

    public void setSUBURBAN(List<GinglesIncomePrecinctData> SUBURBAN) {
        this.SUBURBAN = SUBURBAN;
    }

    public List<GinglesIncomePrecinctData> getURBAN() {
        return URBAN;
    }

    public void setURBAN(List<GinglesIncomePrecinctData> URBAN) {
        this.URBAN = URBAN;
    }

}
