package me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments;

import org.springframework.data.mongodb.core.mapping.Field;

public class EIIncomePartyData {
    public EIIncomePartyData() {
        // No-args constructor
    }
    @Field("REPUBLICAN")
    private EIIncomeRegionData REPUBLICAN;
    

    @Field("DEMOCRATIC")
    private EIIncomeRegionData DEMOCRATIC;

    // Getters and Setters

    public EIIncomeRegionData getREPUBLICAN() {
        return REPUBLICAN;
    }

    public void setREPUBLICAN(EIIncomeRegionData REPUBLICAN) {
        this.REPUBLICAN = REPUBLICAN;
    }

    public EIIncomeRegionData getDEMOCRATIC() {
        return DEMOCRATIC;
    }

    public void setDEMOCRATIC(EIIncomeRegionData DEMOCRATIC) {
        this.DEMOCRATIC = DEMOCRATIC;
    }
}
