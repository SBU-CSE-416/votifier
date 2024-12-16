package me.Votifier.server.model.documents.PlanSplitsDocuments;

import org.springframework.data.mongodb.core.mapping.Field;


public class PlanSplitsData {
    public PlanSplitsData() {
        // No-args constructor
    }
    @Field("ensemble_1")
    private PlanSplitsDataDetails ensemble_1;

    @Field("ensemble_2")

    private PlanSplitsDataDetails ensemble_2;

    //getter and setter

    public PlanSplitsDataDetails getEnsemble_1() {
        return ensemble_1;
    }

    public void setEnsemble_1(PlanSplitsDataDetails ensemble_1) {
        this.ensemble_1 = ensemble_1;
    }

    public PlanSplitsDataDetails getEnsemble_2() {
        return ensemble_2;
    }

    public void setEnsemble_2(PlanSplitsDataDetails ensemble_2) {
        this.ensemble_2 = ensemble_2;
    }
}
