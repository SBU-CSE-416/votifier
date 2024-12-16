package me.Votifier.server.model.documents.BoxplotRacialDocuments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "racial_boxplots_data")
public class BoxplotRacialAnalysis {
    public BoxplotRacialAnalysis() {
        // No-args constructor
    }

    @Id
    private String id;

    @Field("NAME")
    private String NAME;

    @Field("data")
    private BoxplotRacialData data;

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

    public BoxplotRacialData getData() {
        return data;
    }

    public void setData(BoxplotRacialData data) {
        this.data = data;
    }

    
}
