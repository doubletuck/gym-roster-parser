package com.gym.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EventParserTest {

    @Test
    public void positionInputIsNull() {
        assertNull(EventParser.parse(null));
    }

    @Test
    public void positionInputIsEmpty() {
        assertNull(EventParser.parse(""), "Empty string");
        assertNull(EventParser.parse("    "), "Empty string with many spaces");
    }

    @Test
    public void positionReturnedInOlympicOrder() {
        String expectedResult = "AA,VT,UB,BB,FX";
        assertEquals(expectedResult, EventParser.parse("AA, VT, UB, BB, FX"), "Order 1");
        assertEquals(expectedResult, EventParser.parse("VT, UB, BB, FX, AA"), "Order 2");
        assertEquals(expectedResult, EventParser.parse("UB, BB, FX, AA, VT"), "Order 3");
        assertEquals(expectedResult, EventParser.parse("BB, FX, AA, VT, UB"), "Order 4");
        assertEquals(expectedResult, EventParser.parse("FX, AA, VT, UB, BB"), "Order 5");
    }

    @Test
    public void hasDuplicatePositions() {
        assertEquals("UB,FX", EventParser.parse("FX, Floor, UB, Floor Exercise, Bars"), "");
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
        assertNull(EventParser.parse("This / Is / Bogus"), "No valid position values in delimited string");
    }
}
