package me.Votifier.server.model;

import java.util.Map;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum StateFipsCode {
    MARYLAND(24, "maryland"),
    SOUTH_CAROLINA(45, "south_carolina");
    
    private final int fipsCode;
    private final String stateName;
    
    StateFipsCode(int fipsCode, String stateName) {
        this.fipsCode = fipsCode;
        this.stateName = stateName;
    }
    
    public int getFipsCode() {
        return fipsCode;
    }
    
    public String getStateName() {
        return stateName;
    }

    private static final Map<Integer, StateFipsCode> fipsCodeToStateMap = Arrays.stream(values())
        .collect(Collectors.toMap(StateFipsCode::getFipsCode, Function.identity()));

     public static Optional<StateFipsCode> fromFipsCode(int fipsCode) {
        return Optional.ofNullable(fipsCodeToStateMap.get(fipsCode));
    } 
}