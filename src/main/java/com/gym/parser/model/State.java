package com.gym.parser.model;

import lombok.Getter;

@Getter
public enum State {

    AL("Alabama", "Ala."),
    AK("Alaska", "Alaska"),
    AZ("Arizona", "Ariz."),
    AR("Arkansas", "Ark."),
    CA("California", "Calif."),
    CO("Colorado", "Colo."),
    CT("Connecticut", "Conn."),
    DE("Delaware", "Del."),
    DC("District of Columbia", "D.C."),
    FL("Florida", "Fla."),
    GA("Georgia", "Ga."),
    HI("Hawaii", "Hawaii"),
    ID("Idaho", "Idaho"),
    IL("Illinois", "Ill."),
    IN("Indiana", "Ind."),
    IA("Iowa", "Iowa"),
    KS("Kansas", "Kan."),
    KY("Kentucky", "Ky."),
    LA("Louisiana", "La."),
    ME("Maine", "Maine"),
    MD("Maryland", "Md."),
    MA("Massachusetts", "Mass."),
    MI("Michigan", "Mich."),
    MN("Minnesota", "Minn."),
    MS("Mississippi", "Miss."),
    MO("Missouri", "Mo."),
    MT("Montana", "Mont."),
    NE("Nebraska", "Neb."),
    NV("Nevada", "Nev."),
    NH("New Hampshire", "N.H."),
    NJ("New Jersey", "N.J."),
    NM("New Mexico", "N.M."),
    NY("New York", "N.Y."),
    NC("North Carolina", "N.C."),
    ND("North Dakota", "N.D."),
    OH("Ohio", "Ohio"),
    OK("Oklahoma", "Okla."),
    OR("Oregon", "Ore."),
    PA("Pennsylvania", "Pa."),
    PR("Puerto Rico", "P.R."),
    RI("Rhode Island", "R.I."),
    SC("South Carolina", "S.C."),
    SD("South Dakota", "S.D."),
    TN("Tennessee", "Tenn."),
    TX("Texas", "Texas"),
    UT("Utah", "Utah"),
    VT("Vermont", "Vt."),
    VA("Virginia", "Va."),
    WA("Washington", "Wash."),
    WV("West Virginia", "W.Va."),
    WI("Wisconsin", "Wis."),
    WY("Wyoming", "Wyo.");

    private final String longName;
    private final String apName;

    State(String longName, String apName) {
        this.longName = longName;
        this.apName = apName;
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
            for (State state : State.values()) {
                if (state.name().equalsIgnoreCase(text) ||
                        state.longName.equalsIgnoreCase(text) ||
                        state.apName.equalsIgnoreCase(text)) {
                    return state;
                }
            }
        }

        return null;
    }
}
