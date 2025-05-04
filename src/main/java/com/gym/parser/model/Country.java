package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Country codes as defined by <a href="https://www.iban.com/country-codes">IBAN</a>.
 * The enumeration name is, in most cases, the alpha-3 code.
 */
@Getter
public enum Country {

    AUS("Australia"),
    CAN("Canada"),
    CHL("Chile"),
    DEU("Germany", "DE"),
    ENG("England"),
    ESP("Spain"),
    FIN("Finland"),
    FRA("France", "FR"),
    ITA("Italy", "IT"),
    HUN("Hungary", "HU"),
    GBR("Great Britain", "GB", "UK", "United Kingdom"),
    MEX("Mexico"),
    NLD("Netherlands", "NL"),
    NZL("New Zealand", "NZ"),
    ROM("Romania", "RO"),
    SGP("Singapore"),
    THA("Thailand"  ),
    USA("United States of America", "US"),
    VEN("Venezuela");

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
            text = text.trim();
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
