package me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments;

import org.springframework.data.mongodb.core.mapping.Field;
public class EIRacialPartyData {
    public EIRacialPartyData() {
        // No-args constructor
    }

    @Field("REPUBLICAN")
    private EIRacialRegionData REPUBLICAN;
    
    @Field("DEMOCRATIC")
    private EIRacialRegionData DEMOCRATIC;

    // Getters and Setters

    public EIRacialRegionData getREPUBLICAN() {
        return REPUBLICAN;
    }

    public void setREPUBLICAN(EIRacialRegionData REPUBLICAN) {
        this.REPUBLICAN = REPUBLICAN;
    }

    public EIRacialRegionData getDEMOCRATIC() {
        return DEMOCRATIC;
    }

    public void setDEMOCRATIC(EIRacialRegionData DEMOCRATIC) {
        this.DEMOCRATIC = DEMOCRATIC;
    }


}
