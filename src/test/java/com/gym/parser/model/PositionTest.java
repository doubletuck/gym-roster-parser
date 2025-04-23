package com.gym.parser.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PositionTest {
    @Test
    public void findWhenInputIsEmpty() {
        assertNull(Position.find(""), "Empty string");
        assertNull(Position.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(Position.find(null));
    }

    @Test
    public void findLongNameDespiteCase() {
        assertEquals(Position.VT, Position.find("vault"), "Lower case");
        assertEquals(Position.VT, Position.find("VAULT"), "Upper case");
        assertEquals(Position.VT, Position.find("vAuLt"), "Random case");
    }

    @Test
    public void findOtherNameDespiteCase() {
        assertEquals(Position.UB, Position.find("bars"), "Lower case");
        assertEquals(Position.UB, Position.find("BARS"), "Upper case");
        assertEquals(Position.UB, Position.find("bArS"), "Random case");
    }

    @Test
    public void findWhenNotMatching() {
        assertNull(Position.find("BOGUS"));
    }

    @Test
    public void findAllAround() {
        assertEquals(Position.AA, Position.find("AA"), "Name");
        assertEquals(Position.AA, Position.find("All-Around"), "Long name");
        assertEquals(Position.AA, Position.find("All Around"), "Other names");
    }

    @Test
    public void findVault() {
        assertEquals(Position.VT, Position.find("VT"), "Name");
        assertEquals(Position.VT, Position.find("Vault"), "Long name");
        assertEquals(Position.VT, Position.find("V"), "Other names");
    }

    @Test
    public void findUnevenBars() {
        assertEquals(Position.UB, Position.find("UB"), "Name");
        assertEquals(Position.UB, Position.find("Uneven Bars"), "Long name");
        assertEquals(Position.UB, Position.find("Bars"), "Other names");
    }

    @Test
    public void findBalanceBeam() {
        assertEquals(Position.BB, Position.find("BB"), "Name");
        assertEquals(Position.BB, Position.find("Balance Beam"), "Long name");
        assertEquals(Position.BB, Position.find("Beam"), "Other names");
    }

    @Test
    public void findFloorExercise() {
        assertEquals(Position.FX, Position.find("FX"), "Name");
        assertEquals(Position.FX, Position.find("Floor Exercise"), "Long name");
        assertEquals(Position.FX, Position.find("Floor"), "Other names");
    }
}