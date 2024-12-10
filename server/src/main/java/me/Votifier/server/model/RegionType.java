package me.Votifier.server.model;

public enum RegionType {
    RURAL("rural"),
    URBAN("urban"),
    SUBURBAN("suburban");

    private final String type;

    RegionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }    
    
}
