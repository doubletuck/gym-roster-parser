package com.gym.parser.util;

import com.gym.parser.model.Country;
import com.gym.parser.model.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationParserTest {

    @Test
    public void emptyStringLocation() {
        LocationParser p = new LocationParser("");
        p.parse();
        assertNull(p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertNull(p.getCountry(), "Country");
    }

    @Test
    public void nullLocation() {
        LocationParser p = new LocationParser(null);
        p.parse();
        assertNull(p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertNull(p.getCountry(), "Country");
    }

    @Test
    public void townOnlySingleWord() {
        String location = "Rockville";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals(location, p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertNull(p.getCountry(), "Country");
    }

    @Test
    public void townOnlyMultipleWords() {
        String location = "San Luis Obispo";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals(location, p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertNull(p.getCountry(), "Country");
    }

    @Test
    public void stateOnly() {
        String location = "Missouri";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertNull(p.getTown(), "Town");
        assertEquals(State.MO, p.getState(), "State");
        assertEquals(Country.USA, p.getCountry(), "Country");
    }

    @Test
    public void countryOnly() {
        String location = "Canada";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertNull(p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertEquals(Country.CAN, p.getCountry(), "Country");
    }

    @Test
    public void townState() {
        String location = "Upper Marlboro, Maryland";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals("Upper Marlboro", p.getTown(), "Town");
        assertEquals(State.MD, p.getState(), "State");
        assertEquals(Country.USA, p.getCountry(), "Country");
    }

    @Test
    public void townCountry() {
        String location = "Bristol, England";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals("Bristol", p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertEquals(Country.ENG, p.getCountry(), "Country");
    }

    @Test
    public void stateAndNonUsaCountry() {
        // Delaware is a valid USA state, but the country England is provided.
        // Therefore, not a USA state.
        String location = "Delaware, England";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals("Delaware", p.getTown(), "Town");
        assertNull(p.getState(), "State");
        assertEquals(Country.ENG, p.getCountry(), "Country");
    }

    @Test
    public void locationUsingNonCommaSeparators() {
        String location = "Dayton OH USA";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals(location, p.getTown(), "Town (with space separator)");
        assertNull(p.getState(), "State (with space separator)");
        assertNull(p.getCountry(), "Country (with space separator)");

        location = "Dayton / OH / USA";
        p = new LocationParser(location);
        p.parse();
        assertEquals(location, p.getTown(), "Town (with forward slash separator)");
        assertNull(p.getState(), "State (with forward slash separator)");
        assertNull(p.getCountry(), "Country (with forward slash separator)");
    }

    @Test
    public void locationWithExtraWhitespace() {
        String location = "   Phoenix  ,  Arizona   ,   USA   ";
        LocationParser p = new LocationParser(location);
        p.parse();
        assertEquals("Phoenix", p.getTown(), "Town");
        assertEquals(State.AZ, p.getState(), "State");
        assertEquals(Country.USA, p.getCountry(), "Country");
    }
}
