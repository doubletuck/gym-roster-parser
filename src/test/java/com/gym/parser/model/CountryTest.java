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
        assertEquals(Country.GBR, Country.find("united kingdom"), "Lower case");
        assertEquals(Country.GBR, Country.find("UNITED KINGDOM"), "Upper case");
        assertEquals(Country.GBR, Country.find("unITEd KIngdoM"), "Random case");
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
    }

    @Test
    public void findChile() {
        assertEquals(Country.CHL, Country.find("CHL"), "Name");
        assertEquals(Country.CHL, Country.find("Chile"), "Long Name");
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
    public void findFinland() {
        assertEquals(Country.FIN, Country.find("FIN"), "Name");
        assertEquals(Country.FIN, Country.find("Finland"), "Long Name");
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
    public void findLatvia() {
        assertEquals(Country.LVA, Country.find("LVA"), "Name");
        assertEquals(Country.LVA, Country.find("Latvia"), "Long Name");
    }

    @Test
    public void findMexico() {
        assertEquals(Country.MEX, Country.find("MEX"), "Name");
        assertEquals(Country.MEX, Country.find("Mexico"), "Long Name");
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
    public void findSingapore() {
        assertEquals(Country.SGP, Country.find("SGP"), "Name");
        assertEquals(Country.SGP, Country.find("Singapore"), "Long Name");
    }

    @Test
    public void findSpain() {
        assertEquals(Country.ESP, Country.find("ESP"), "Name");
        assertEquals(Country.ESP, Country.find("Spain"), "Long Name");
    }

    @Test
    public void findThailand() {
        assertEquals(Country.THA, Country.find("THA"), "Name");
        assertEquals(Country.THA, Country.find("Thailand"), "Long Name");
    }

    @Test
    public void findUnitedStates() {
        assertEquals(Country.USA, Country.find("USA"), "Name");
        assertEquals(Country.USA, Country.find("United States of America"), "Long Name");
        assertEquals(Country.USA, Country.find("US"), "Other name - 2 Character Code");
    }

    @Test
    public void findVenezuela() {
        assertEquals(Country.VEN, Country.find("VEN"), "Name");
        assertEquals(Country.VEN, Country.find("Venezuela"), "Long Name");
    }
}
