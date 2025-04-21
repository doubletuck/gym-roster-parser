package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Position {

    AA("All-Around", "All Around"),
    VT("Vault"),
    UB("Uneven Bars", "Bars"),
    BB("Balance Beam", "Beam"),
    FL("Floor Exercise", "Floor");

    private final String longName;
    private final String[] otherNames;

    Position(String longName, String... otherNames) {
        this.longName = longName;
        this.otherNames = otherNames;
    }

    /**
     * Returns the Position enum that matches the given text.
     *
     * @param   text A position, case insensitive.
     * @return  The Position enum that matches the given text or null
     *          if not matches are found.
     */
    public static Position find(String text) {
        if (text != null && !text.isEmpty()) {
            text = text.trim();
            for (Position position : values()) {
                if (position.name().equalsIgnoreCase(text) ||
                        position.longName.equalsIgnoreCase(text) ||
                        StringUtils.equalsAnyIgnoreCase(text, position.otherNames)) {
                    return position;
                }
            }
        }
        return null;
    }
}
