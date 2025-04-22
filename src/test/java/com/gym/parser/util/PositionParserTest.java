package com.gym.parser.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PositionParserTest {

    @Test
    public void positionInputIsNull() {
        assertNull(PositionParser.parse(null));
    }

    @Test
    public void positionInputIsEmpty() {
        assertNull(PositionParser.parse(""), "Empty string");
        assertNull(PositionParser.parse("    "), "Empty string with many spaces");
    }

    @Test
    public void positionReturnedInOlympicOrder() {
        String expectedResult = "AA,VT,UB,BB,FX";
        assertEquals(expectedResult, PositionParser.parse("AA, VT, UB, BB, FX"), "Order 1");
        assertEquals(expectedResult, PositionParser.parse("VT, UB, BB, FX, AA"), "Order 2");
        assertEquals(expectedResult, PositionParser.parse("UB, BB, FX, AA, VT"), "Order 3");
        assertEquals(expectedResult, PositionParser.parse("BB, FX, AA, VT, UB"), "Order 4");
        assertEquals(expectedResult, PositionParser.parse("FX, AA, VT, UB, BB"), "Order 5");
    }

    @Test
    public void hasDuplicatePositions() {
        assertEquals("UB,FX", PositionParser.parse("FX, Floor, UB, Floor Exercise, Bars"), "");
    }

    @Test
    public void testAcceptableDelimiters() {
        assertEquals("VT,FX", PositionParser.parse("FX, VT"), "Comma delimiter with space");
        assertEquals("VT,FX", PositionParser.parse("FX,VT"), "Comma delimiter no space");
        assertEquals("VT,FX", PositionParser.parse("FX / VT"), "Forward slash delimiter with space");
        assertEquals("VT,FX", PositionParser.parse("FX/VT"), "Forward slash delimiter no space");
        assertEquals("VT,BB,FX", PositionParser.parse("FX/VT, BB"), "Mixed delimiters");
    }

    @Test
    public void testWithAnUnrecognizedDelimiter() {
        assertNull(PositionParser.parse("VT FX VT"), "Valid values but unrecognized space delimiter");
    }

    @Test
    public void ignoresBogusValues() {
        assertEquals("UB,FX", PositionParser.parse("BOGUS, FX, badder, BARS, BAD"), "Bogus values interspersed");
        assertNull(PositionParser.parse("This / Is / Bogus"), "No valid position values in delimited string");
    }
}
