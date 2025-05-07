package com.gym.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EventParserTest {

    @Test
    public void inputIsNull() {
        assertNull(EventParser.parse(null));
    }

    @Test
    public void inputIsEmpty() {
        assertNull(EventParser.parse(""), "Empty string");
        assertNull(EventParser.parse("    "), "Empty string with many spaces");
    }

    @Test
    public void valueReturnedInOlympicOrder() {
        String expectedResult = "AA,VT,UB,BB,FX";
        assertEquals(expectedResult, EventParser.parse("AA, VT, UB, BB, FX"), "Order 1");
        assertEquals(expectedResult, EventParser.parse("VT, UB, BB, FX, AA"), "Order 2");
        assertEquals(expectedResult, EventParser.parse("UB, BB, FX, AA, VT"), "Order 3");
        assertEquals(expectedResult, EventParser.parse("BB, FX, AA, VT, UB"), "Order 4");
        assertEquals(expectedResult, EventParser.parse("FX, AA, VT, UB, BB"), "Order 5");
    }

    @Test
    public void hasDuplicateEvents() {
        assertEquals("UB,FX", EventParser.parse("FX, Floor, UB, Floor Exercise, Bars"));
    }

    @Test
    public void testAcceptableDelimiters() {
        assertEquals("VT,FX", EventParser.parse("FX, VT"), "Comma delimiter with space");
        assertEquals("VT,FX", EventParser.parse("FX,VT"), "Comma delimiter no space");
        assertEquals("VT,FX", EventParser.parse("FX / VT"), "Forward slash delimiter with space");
        assertEquals("VT,FX", EventParser.parse("FX/VT"), "Forward slash delimiter no space");
        assertEquals("VT,BB,FX", EventParser.parse("FX/VT, BB"), "Mixed delimiters");
    }

    @Test
    public void testWithAnUnrecognizedDelimiter() {
        assertNull(EventParser.parse("VT FX VT"), "Valid values but unrecognized space delimiter");
    }

    @Test
    public void ignoresBogusValues() {
        assertEquals("UB,FX", EventParser.parse("BOGUS, FX, badder, BARS, BAD"), "Bogus values interspersed");
        assertNull(EventParser.parse("This / Is / Bogus"), "No valid event values in delimited string");
    }

    @Test
    public void oneElementCombinations() {
        assertEquals("AA", EventParser.parse("AA"), "AA");
        assertEquals("VT", EventParser.parse("VT"), "VT");
        assertEquals("UB", EventParser.parse("UB"), "UB");
        assertEquals("BB", EventParser.parse("BB"), "BB");
        assertEquals("FX", EventParser.parse("FX"), "FX");
    }

    @Test
    public void twoElementCombinations() {
        assertEquals("AA,VT", EventParser.parse("AA,VT"), "AA,VT");
        assertEquals("AA,UB", EventParser.parse("AA,UB"), "AA,UB");
        assertEquals("AA,BB", EventParser.parse("AA,BB"), "AA,BB");
        assertEquals("AA,FX", EventParser.parse("AA,FX"), "AA,FX");
        assertEquals("VT,UB", EventParser.parse("VT,UB"), "VT,UB");
        assertEquals("VT,BB", EventParser.parse("VT,BB"), "VT,BB");
        assertEquals("VT,FX", EventParser.parse("VT,FX"), "VT,FX");
        assertEquals("UB,BB", EventParser.parse("UB,BB"), "UB,BB");
        assertEquals("UB,FX", EventParser.parse("UB,FX"), "UB,FX");
        assertEquals("BB,FX", EventParser.parse("BB,FX"), "BB,FX");
    }

    @Test
    public void threeElementCombinations() {
        assertEquals("AA,VT,UB", EventParser.parse("AA,VT,UB"), "AA,VT,UB");
        assertEquals("AA,VT,BB", EventParser.parse("AA,VT,BB"), "AA,VT,BB");
        assertEquals("AA,VT,FX", EventParser.parse("AA,VT,FX"), "AA,VT,FX");
        assertEquals("AA,UB,BB", EventParser.parse("AA,UB,BB"), "AA,UB,BB");
        assertEquals("AA,UB,FX", EventParser.parse("AA,UB,FX"), "AA,UB,FX");
        assertEquals("AA,BB,FX", EventParser.parse("AA,BB,FX"), "AA,BB,FX");
        assertEquals("VT,UB,FX", EventParser.parse("VT,UB,FX"), "VT,UB,FX");
        assertEquals("VT,BB,FX", EventParser.parse("VT,BB,FX"), "VT,BB,FX");
        assertEquals("UB,BB,FX", EventParser.parse("UB,BB,FX"), "UB,BB,FX");
    }

    @Test
    public void fourElementCombinations() {
        assertEquals("AA,VT,UB,BB", EventParser.parse("AA,VT,UB,BB"), "AA,VT,UB,BB");
        assertEquals("AA,VT,UB,FX", EventParser.parse("AA,VT,UB,FX"), "AA,VT,UB,FX");
        assertEquals("AA,VT,BB,FX", EventParser.parse("AA,VT,BB,FX"), "AA,VT,BB,FX");
        assertEquals("AA,UB,BB,FX", EventParser.parse("AA,UB,BB,FX"), "AA,UB,BB,FX");
        assertEquals("VT,UB,BB,FX", EventParser.parse("VT,UB,BB,FX"), "VT,UB,BB,FX");
    }

    @Test
    public void fiveElementCombinations() {
        assertEquals("AA,VT,UB,BB,FX", EventParser.parse("AA,VT,UB,BB,FX"), "AA,VT,UB,BB,FX");
    }
}
