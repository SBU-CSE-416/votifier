package me.Votifier.server.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RacialGroup {
    WHITE("White", "WHT_NHSP22"),
    BLACK("Black/African American", "BLK_NHSP22"),
    HISPANIC_LATINO("Hispanic/Latino", "HIS_POP22"),
    ASIAN("Asian", "ASN_NHSP22"),
    PACIFIC_ISLANDER("Hawaiian/Other Pacific Islander", "HPI_NHSP22"),
    NATIVE_AMERICAN("Native American/Alaskan Native", "AIA_NHSP22");
    
    private final String formattedRacialGroupName;
    private final String identifier;
    
    RacialGroup(String formattedRacialGroupName, String identifier) {
        this.formattedRacialGroupName = formattedRacialGroupName;
        this.identifier = identifier;
    }
    
    public String getFormattedRacialGroupName() {
        return formattedRacialGroupName;
    }

    public String getIdentifier() {
        return identifier;
    }

    private static final Map<String, RacialGroup> formattedRacialGroupNameToEnum = Arrays.stream(values())
        .collect(Collectors.toMap(RacialGroup::getFormattedRacialGroupName, Function.identity()));

     public static Optional<RacialGroup> fromFormattedRacialGroupName(String formattedRacialGroupName) {
        return Optional.ofNullable(formattedRacialGroupNameToEnum.get(formattedRacialGroupName));
    } 
}