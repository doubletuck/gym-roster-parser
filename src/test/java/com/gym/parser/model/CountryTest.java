package com.gym.parser.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CountryTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(Country.find(""), "Empty string");
        assertNull(Country.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(Country.find(null));
    }

    @Test
    public void findLongNameDespiteCase() {
        assertEquals(Country.CAN, Country.find("canada"), "Lower case");
        assertEquals(Country.CAN, Country.find("CANADA"), "Upper case");
        assertEquals(Country.CAN, Country.find("caNADa"), "Random case");
    }

    @Test
    public void findOtherNameDespiteCase() {
        assertEquals(Country.CAN, Country.find("ca"), "Lower case");
        assertEquals(Country.CAN, Country.find("CA"), "Upper case");
        assertEquals(Country.CAN, Country.find("Ca"), "Random case");
    }

    @Test
    public void findAustralia() {
        assertEquals(Country.AUS, Country.find("AUS"), "Name");
        assertEquals(Country.AUS, Country.find("Australia"), "Long Name");
    }

    @Test
    public void findCanada() {
        assertEquals(Country.CAN, Country.find("CAN"), "Name");
        assertEquals(Country.CAN, Country.find("Canada"), "Long Name");
        assertEquals(Country.CAN, Country.find("CA"), "Other name - 2 Character Code");
    }

    @Test
    public void findGermany() {
        assertEquals(Country.DEU, Country.find("DEU"), "Name");
        assertEquals(Country.DEU, Country.find("Germany"), "Long Name");
        assertEquals(Country.DEU, Country.find("DE"), "Other name - 2 Character Code");
    }

    @Test
    public void findEngland() {
        assertEquals(Country.ENG, Country.find("ENG"), "Name");
        assertEquals(Country.ENG, Country.find("England"), "Long Name");
    }

    @Test
    public void findHungary() {
        assertEquals(Country.HUN, Country.find("HUN"), "Name");
        assertEquals(Country.HUN, Country.find("Hungary"), "Long Name");
        assertEquals(Country.HUN, Country.find("HU"), "Other name - 2 Character Code");
    }

    @Test
    public void findGreatBritain() {
        assertEquals(Country.GBR, Country.find("GBR"), "Name");
        assertEquals(Country.GBR, Country.find("Great Britain"), "Long Name");
        assertEquals(Country.GBR, Country.find("GB"), "Other name - 2 Character Code");
        assertEquals(Country.GBR, Country.find("UK"), "Other name - UK");
        assertEquals(Country.GBR, Country.find("United Kingdom"), "Other name - United Kingdom");
    }

    @Test
    public void findNetherlands() {
        assertEquals(Country.NLD, Country.find("NLD"), "Name");
        assertEquals(Country.NLD, Country.find("Netherlands"), "Long Name");
        assertEquals(Country.NLD, Country.find("NL"), "Other name - 2 Character Code");
    }

    @Test
    public void findNewZealand() {
        assertEquals(Country.NZL, Country.find("NZL"), "Name");
        assertEquals(Country.NZL, Country.find("New Zealand"), "Long Name");
        assertEquals(Country.NZL, Country.find("NZ"), "Other name - 2 Character Code");
    }

    @Test
    public void findRomania() {
        assertEquals(Country.ROM, Country.find("ROM"), "Name");
        assertEquals(Country.ROM, Country.find("Romania"), "Long Name");
        assertEquals(Country.ROM, Country.find("RO"), "Other name - 2 Character Code");
    }

    @Test
    public void findUnitedStates() {
        assertEquals(Country.USA, Country.find("USA"), "Name");
        assertEquals(Country.USA, Country.find("United States of America"), "Long Name");
        assertEquals(Country.USA, Country.find("US"), "Other name - 2 Character Code");
    }
}
