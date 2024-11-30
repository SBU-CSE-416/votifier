package me.Votifier.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "precincts_boundaries")
public class PrecinctsBoundary {

    @Id
    private String id; // Matches _id in MongoDB

    private String type; // Should be "Feature"
    private Object properties; // Holds the properties object from GeoJSON
    private Object geometry; // Holds the geometry object from GeoJSON
    private String NAME; // State name

    // Getters and Setters
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

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Object getGeometry() {
        return geometry;
    }

    public void setGeometry(Object geometry) {
        this.geometry = geometry;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
