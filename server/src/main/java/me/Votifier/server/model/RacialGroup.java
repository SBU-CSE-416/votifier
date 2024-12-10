package me.Votifier.server.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RacialGroup {
    WHITE("White", "WHT_NHSP22", "WHITE"),
    BLACK("Black/African American", "BLK_NHSP22", "BLACK"),
    HISPANIC_LATINO("Hispanic/Latino", "HIS_POP22", "HISPANIC"),
    ASIAN("Asian", "ASN_NHSP22", "ASIAN"),;
    
    private final String formattedRacialGroupName;
    private final String identifier;
    private final String ginglesIdentifer;
    
    RacialGroup(String formattedRacialGroupName, String identifier, String ginglesIdentifer) {
        this.formattedRacialGroupName = formattedRacialGroupName;
        this.identifier = identifier;
        this.ginglesIdentifer = ginglesIdentifer;
    }
    
    public String getFormattedRacialGroupName() {
        return formattedRacialGroupName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getGinglesIdentifer() {
        return ginglesIdentifer;
    }
    private static final Map<String, RacialGroup> formattedRacialGroupNameToEnum = Arrays.stream(values())
        .collect(Collectors.toMap(RacialGroup::getFormattedRacialGroupName, Function.identity()));

     public static Optional<RacialGroup> fromFormattedRacialGroupName(String formattedRacialGroupName) {
        return Optional.ofNullable(formattedRacialGroupNameToEnum.get(formattedRacialGroupName));
    } 
}