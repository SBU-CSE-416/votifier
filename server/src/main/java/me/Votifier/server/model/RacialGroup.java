package me.Votifier.server.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RacialGroup {
    WHITE("White"),
    BLACK("Black/African American"),
    HISPANIC_LATINO("Hispanic/Latino"),
    ASIAN("Asian"),
    PACIFIC_ISLANDER("Hawaiian/Other Pacific Islander"),
    NATIVE_AMERICAN("Native American/Alaskan Native");
    
    private final String formattedRacialGroupName;
    
    RacialGroup(String formattedRacialGroupName) {
        this.formattedRacialGroupName = formattedRacialGroupName;
    }
    
    public String getFormattedRacialGroupName() {
        return formattedRacialGroupName;
    }

    private static final Map<String, RacialGroup> formattedRacialGroupNameToEnum = Arrays.stream(values())
        .collect(Collectors.toMap(RacialGroup::getFormattedRacialGroupName, Function.identity()));

     public static Optional<RacialGroup> fromFormattedRacialGroupName(String formattedRacialGroupName) {
        return Optional.ofNullable(formattedRacialGroupNameToEnum.get(formattedRacialGroupName));
    } 
}