package com.gym.parser.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Country codes as defined by IBAN: https://www.iban.com/country-codes.
 */
@Getter
public enum Country {

    CAN("CA", "CAN", "Canada"),
    ENG(null, null, "England"),
    HUN("HU", "HUN", "Hungary"),
    GBR("GB", "GBR", "United Kingdom of Great Britain and Northern Ireland"),
    NZL("NZ", "NZL", "New Zealand"),
    ROM("RO", "ROM", "Romania"),
    USA("US", "USA", "United States of America");

    private final String alpha2Code;
    private final String alpha3Code;
    private final String countryName;

    Country(String alpha2Code, String alpha3Code, String countryName) {
        this.alpha2Code = alpha2Code;
        this.alpha3Code = alpha3Code;
        this.countryName = countryName;
    }

    /**
     * Returns the Country enum that matches the given text.
     *
     * @param text The country alpha2, alpha3 or country name.
     * @return The Country enum that matches the given text or null if no
     * matches are found.
     */
    public static Country find(String text) {
        if (text != null && !text.isEmpty()) {
            for (Country country : values()) {
                if (text.equalsIgnoreCase(country.getAlpha2Code()) ||
                        text.equalsIgnoreCase(country.getAlpha3Code()) ||
                        text.equalsIgnoreCase(country.getCountryName())) {
                    return country;
                }
            }
        }

        return null;
    }
}
