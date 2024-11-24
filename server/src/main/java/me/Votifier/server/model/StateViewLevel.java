package me.Votifier.server.model;

public enum StateViewLevel {
    STATE("_state.geojson"),
    DISTRICTS("_cd.geojson"),
    PRECINCTS("_precincts.geojson");

    private final String fileExtensionSegment;
    
    StateViewLevel(String fileExtensionSegment) {
        this.fileExtensionSegment = fileExtensionSegment;
    }
    
    public String getFileExtensionSegment() {
        return fileExtensionSegment;
    }
}
