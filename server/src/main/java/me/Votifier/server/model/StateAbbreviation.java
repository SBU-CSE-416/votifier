package me.Votifier.server.model;

public enum StateAbbreviation {
    MD(24, "Maryland"),
    SC(45, "South Carolina");
    
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