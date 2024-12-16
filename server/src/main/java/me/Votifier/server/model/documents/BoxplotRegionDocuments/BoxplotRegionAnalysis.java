package me.Votifier.server.model.documents.BoxplotRegionDocuments;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "region_type_boxplots_data")
public class BoxplotRegionAnalysis {
    public BoxplotRegionAnalysis() {
        // No-args constructor
    }

    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("data")
    private BoxplotRegionData data;

    //getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public BoxplotRegionData getData() {
        return data;
    }

    public void setData(BoxplotRegionData data) {
        this.data = data;
    }

    
}