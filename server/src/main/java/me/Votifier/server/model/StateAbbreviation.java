package me.Votifier.server.model;

import java.util.Map;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    // private static final Map<Integer, StateAbbreviation> fipsCodeToState = Arrays.stream(values())
    //     .collect(Collectors.toMap(StateAbbreviation::getFipsCode, Function.identity()));

    //  public static Optional<StateFipsCode> fromFipsCode(int fipsCode) {
    //     return Optional.ofNullable(fipsCodeToState.get(fipsCode));
    // } 
}