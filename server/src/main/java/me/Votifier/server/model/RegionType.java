package me.Votifier.server.model;

public enum RegionType {
    ALL ("ALL"),
    RURAL("RURAL"),
    URBAN("URBAN"),
    SUBURBAN("SUBURBAN"),;

    private final String type;

    RegionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }    
    
}
