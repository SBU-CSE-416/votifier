package me.Votifier.server.model.documents.BoxplotRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class BoxplotRacialData {
    public BoxplotRacialData() {
        // No-args constructor
    }
    @Field("ensemble_1")
    private BoxplotRacialEnsembleRace ensemble_1;

    @Field("ensemble_2")

    private BoxplotRacialEnsembleRace ensemble_2;

    //getter and setter

    public BoxplotRacialEnsembleRace getEnsemble_1() {
        return ensemble_1;
    }

    public void setEnsemble_1(BoxplotRacialEnsembleRace ensemble_1) {
        this.ensemble_1 = ensemble_1;
    }

    public BoxplotRacialEnsembleRace getEnsemble_2() {
        return ensemble_2;
    }

    public void setEnsemble_2(BoxplotRacialEnsembleRace ensemble_2) {
        this.ensemble_2 = ensemble_2;
    }
}
