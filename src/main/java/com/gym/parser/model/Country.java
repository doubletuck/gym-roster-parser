package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Country codes as defined by <a href="https://www.iban.com/country-codes">IBAN</a>.
 * The enumeration name is, in most cases, the alpha-3 code.
 */
@Getter
public enum Country {

    CAN("Canada", "CA"),
    ENG("England"),
    HUN("Hungary", "HU"),
    GBR("Great Britain", "GB"),
    NZL("New Zealand", "NZ"),
    ROM("Romania", "RO"),
    USA("United States of America", "US");

    private final String longName;
    private final String[] otherNames;

    Country(String longName, String... otherNames) {
        this.longName = longName;
        this.otherNames = otherNames;
    }

    /**
     * Returns the Country enum that matches the given text.
     *
     * @param   text The country alpha2, alpha3 or country name, case
     *          insensitive.
     * @return  The Country enum that matches the given text or null if no
     *          matches are found.
     */
    public static Country find(String text) {
        if (text != null && !text.isEmpty()) {
            for (Country country : values()) {
                if (country.name().equalsIgnoreCase(text) ||
                        country.longName.equalsIgnoreCase(text) ||
                        StringUtils.equalsAnyIgnoreCase(text, country.otherNames)) {
                    return country;
                }
            }
        }

        return null;
    }
}
