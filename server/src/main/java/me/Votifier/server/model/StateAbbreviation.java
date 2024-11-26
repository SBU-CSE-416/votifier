package me.Votifier.server.model;

public enum StateAbbreviation {
    MD(24, "maryland"),
    SC(45, "south_carolina");
    
    private final int stateFipsCode;
    private final String fullStateName;
    
    StateAbbreviation(int stateFipsCode, String fullStateName) {
        this.stateFipsCode = stateFipsCode;
        this.fullStateName = fullStateName;
    }
    
    public int getStateFipsCode() {
        return stateFipsCode;
    }
    
    public String getFullStateName() {
        return fullStateName;
    }
}