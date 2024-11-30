package me.Votifier.server.model;

public enum StateViewLevel {
    STATE("state_boundaries"),
    DISTRICTS("cd_boundaries"),
    PRECINCTS("precinct_boundaries");

    private final String fileExtensionSegment;
    
    StateViewLevel(String fileExtensionSegment) {
        this.fileExtensionSegment = fileExtensionSegment;
    }
    
    public String getFileExtensionSegment() {
        return fileExtensionSegment;
    }
}
