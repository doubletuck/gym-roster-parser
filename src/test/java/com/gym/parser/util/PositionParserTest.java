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
        String expectedResult = "AA,VT,UB,BB,FL";
        assertEquals(expectedResult, PositionParser.parse("AA, VT, UB, BB, FL"), "Order 1");
        assertEquals(expectedResult, PositionParser.parse("VT, UB, BB, FL, AA"), "Order 2");
        assertEquals(expectedResult, PositionParser.parse("UB, BB, FL, AA, VT"), "Order 3");
        assertEquals(expectedResult, PositionParser.parse("BB, FL, AA, VT, UB"), "Order 4");
        assertEquals(expectedResult, PositionParser.parse("FL, AA, VT, UB, BB"), "Order 5");
    }

    @Test
    public void hasDuplicatePositions() {
        assertEquals("UB,FL", PositionParser.parse("FL, Floor, UB, Floor Exercise, Bars"), "");
    }

    @Test
    public void testAcceptableDelimiters() {
        assertEquals("VT,FL", PositionParser.parse("FL, VT"), "Comma delimiter with space");
        assertEquals("VT,FL", PositionParser.parse("FL,VT"), "Comma delimiter no space");
        assertEquals("VT,FL", PositionParser.parse("FL / VT"), "Forward slash delimiter with space");
        assertEquals("VT,FL", PositionParser.parse("FL/VT"), "Forward slash delimiter no space");
        assertEquals("VT,BB,FL", PositionParser.parse("FL/VT, BB"), "Mixed delimiters");
    }

    @Test
    public void testWithAnUnrecognizedDelimiter() {
        assertNull(PositionParser.parse("VT FL VT"), "Valid values but unrecognized space delimiter");
    }

    @Test
    public void ignoresBogusValues() {
        assertEquals("UB,FL", PositionParser.parse("BOGUS, FL, badder, BARS, BAD"), "Bogus values interspersed");
        assertNull(PositionParser.parse("This / Is / Bogus"), "No valid position values in delimited string");
    }
}
