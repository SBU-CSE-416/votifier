package me.Votifier.server.model.documents.BoxplotRegionDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class BoxplotRegionData {
    public BoxplotRegionData() {
        // No-args constructor
    }
    @Field("ensemble_1")
    private BoxplotEnsembleRegion ensemble_1;

    @Field("ensemble_2")

    private BoxplotEnsembleRegion ensemble_2;

    //getter and setter

    public BoxplotEnsembleRegion getEnsemble_1() {
        return ensemble_1;
    }

    public void setEnsemble_1(BoxplotEnsembleRegion ensemble_1) {
        this.ensemble_1 = ensemble_1;
    }

    public BoxplotEnsembleRegion getEnsemble_2() {
        return ensemble_2;
    }

    public void setEnsemble_2(BoxplotEnsembleRegion ensemble_2) {
        this.ensemble_2 = ensemble_2;
    }
}

