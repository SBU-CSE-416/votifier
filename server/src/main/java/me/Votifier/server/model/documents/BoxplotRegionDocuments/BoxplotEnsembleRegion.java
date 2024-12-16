package me.Votifier.server.model.documents.BoxplotRegionDocuments;
import org.springframework.data.mongodb.core.mapping.Field;
public class BoxplotEnsembleRegion {
    public BoxplotEnsembleRegion() {
        // No-args constructor
    }

    @Field("RURAL")
    private BoxplotRegionPlotData RURAL;

    @Field("SUBURBAN")
    private BoxplotRegionPlotData SUBURBAN;

    @Field("URBAN")

    private BoxplotRegionPlotData URBAN;

    // Getters and Setters

    public BoxplotRegionPlotData getRURAL() {
        return RURAL;
    }

    public void setRURAL(BoxplotRegionPlotData RURAL) {
        this.RURAL = RURAL;
    }

    public BoxplotRegionPlotData getSUBURBAN() {
        return SUBURBAN;
    }

    public void setSUBURBAN(BoxplotRegionPlotData SUBURBAN) {
        this.SUBURBAN = SUBURBAN;
    }

    public BoxplotRegionPlotData getURBAN() {
        return URBAN;
    }

    public void setURBAN(BoxplotRegionPlotData URBAN) {
        this.URBAN = URBAN;
    }
}
