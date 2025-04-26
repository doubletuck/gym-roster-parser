package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum State {

    AL("Alabama", "Ala"),
    AK("Alaska"),
    AZ("Arizona", "Ariz"),
    AR("Arkansas", "Ark"),
    CA("California", "Calif"),
    CO("Colorado", "Colo"),
    CT("Connecticut", "Conn"),
    DE("Delaware", "Del"),
    DC("District of Columbia"),
    FL("Florida", "Fla"),
    GA("Georgia"),
    HI("Hawaii", "Hawai'i"),
    ID("Idaho"),
    IL("Illinois", "Ill"),
    IN("Indiana", "Ind"),
    IA("Iowa"),
    KS("Kansas", "Kan"),
    KY("Kentucky"),
    LA("Louisiana"),
    ME("Maine"),
    MD("Maryland"),
    MA("Massachusetts", "Mass"),
    MI("Michigan", "Mich"),
    MN("Minnesota", "Minn"),
    MS("Mississippi", "Miss"),
    MO("Missouri"),
    MT("Montana", "Mont"),
    NE("Nebraska", "Neb"),
    NV("Nevada", "Nev"),
    NH("New Hampshire"),
    NJ("New Jersey"),
    NM("New Mexico"),
    NY("New York"),
    NC("North Carolina"),
    ND("North Dakota"),
    OH("Ohio"),
    OK("Oklahoma", "Okla"),
    OR("Oregon", "Ore"),
    PA("Pennsylvania", "Penn"),
    PR("Puerto Rico"),
    RI("Rhode Island"),
    SC("South Carolina"),
    SD("South Dakota"),
    TN("Tennessee", "Tenn"),
    TX("Texas", "Tex"),
    UT("Utah"),
    VT("Vermont"),
    VA("Virginia"),
    WA("Washington", "Wash"),
    WV("West Virginia", "WVa"),
    WI("Wisconsin", "Wis", "Wisc"),
    WY("Wyoming", "Wyo");

    private final String longName;
    private final String[] otherNames;

    State(String longName, String... otherNames) {
        this.longName = longName;
        this.otherNames = otherNames;
    }

    /**
     * Returns the State enum that matches the given text.
     *
     * @param text The state code or long name.
     * @return The State enum that matches the given text or null if no
     * matches are found.
     */
    public static State find(String text) {

        if (text != null && !text.isEmpty()) {
            text = text.trim().replace(".","");
            for (State state : State.values()) {
                if (state.name().equalsIgnoreCase(text) ||
                        state.longName.equalsIgnoreCase(text) ||
                        StringUtils.equalsAnyIgnoreCase(text, state.otherNames)) {
                    return state;
                }
            }
        }

        return null;
    }
}
