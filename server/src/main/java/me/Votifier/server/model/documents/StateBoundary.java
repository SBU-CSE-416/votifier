package me.Votifier.server.model.documents;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "state_boundaries")
public class StateBoundary {
    @Id
    private String id; // Maps to MongoDB "_id"

    private String type;
    private Object features;
    private String NAME; // Matches the "NAME" field in MongoDB

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getFeatures() {
        return features;
    }

    public void setFeatures(Object features) {
        this.features = features;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}  