package me.Votifier.server.model.documents.BoxplotIncomeDocuments;


import org.springframework.data.mongodb.core.mapping.Field;

public class BoxplotIncomeData {
    public BoxplotIncomeData() {
        // No-args constructor
    }
    @Field("ensemble_1")
    private BoxplotEnsembleIncome ensemble_1;

    @Field("ensemble_2")

    private BoxplotEnsembleIncome ensemble_2;

    //getter and setter

    public BoxplotEnsembleIncome getEnsemble_1() {
        return ensemble_1;
    }

    public void setEnsemble_1(BoxplotEnsembleIncome ensemble_1) {
        this.ensemble_1 = ensemble_1;
    }

    public BoxplotEnsembleIncome getEnsemble_2() {
        return ensemble_2;
    }

    public void setEnsemble_2(BoxplotEnsembleIncome ensemble_2) {
        this.ensemble_2 = ensemble_2;
    }
}
