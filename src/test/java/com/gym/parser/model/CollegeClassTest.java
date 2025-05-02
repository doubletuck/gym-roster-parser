package com.gym.parser.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CollegeClassTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(CollegeClass.find(""), "Empty string");
        assertNull(CollegeClass.find("   "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(CollegeClass.find(null));
    }

    @Test
    public void findLongNameDespiteCase() {
        assertEquals(CollegeClass.GR, CollegeClass.find("graduate student"), "Lower case");
        assertEquals(CollegeClass.GR, CollegeClass.find("GRADUATE STUDENT"), "Upper case");
        assertEquals(CollegeClass.GR, CollegeClass.find("graDUaTe STudenT"), "Mixed case");
    }

    @Test
    public void findOtherNameDespiteCase() {
        assertEquals(CollegeClass.GR, CollegeClass.find("graduate"), "Other name - Graduate - Lower case");
        assertEquals(CollegeClass.GR, CollegeClass.find("GRADUATE"), "Other name - Graduate - Upper case");
        assertEquals(CollegeClass.GR, CollegeClass.find("graDUaTe"), "Other name - Graduate - Mixed case");
        assertEquals(CollegeClass.GR, CollegeClass.find("gr."), "Other name - Gr - Lower case");
        assertEquals(CollegeClass.GR, CollegeClass.find("GR."), "Other name - Gr - Upper case");
        assertEquals(CollegeClass.GR, CollegeClass.find("gR."), "Other name - Gr - Mixed case");
    }

    @Test
    public void findFreshman() {
        assertEquals(CollegeClass.FR, CollegeClass.find("FR"), "By Name");
        assertEquals(CollegeClass.FR, CollegeClass.find("Freshman"), "By Long Name");
        assertEquals(CollegeClass.FR, CollegeClass.find("Fr."), "By Other Name");
    }

    @Test
    public void findRedshirtFreshman() {
        assertEquals(CollegeClass.REDSHIRT_FR, CollegeClass.find("REDSHIRT_FR"), "By Name");
        assertEquals(CollegeClass.REDSHIRT_FR, CollegeClass.find("Redshirt Freshman"), "By Long Name");
        assertEquals(CollegeClass.REDSHIRT_FR, CollegeClass.find("R-Fr."), "By Other Name - R-Fr.");
        assertEquals(CollegeClass.REDSHIRT_FR, CollegeClass.find("RS Fr."), "By Other Name - RS Fr.");
        assertEquals(CollegeClass.REDSHIRT_FR, CollegeClass.find("R-Freshman"), "By Other Name - R-Freshman");
    }

    @Test
    public void findSophomore() {
        assertEquals(CollegeClass.SO, CollegeClass.find("SO"), "By Name");
        assertEquals(CollegeClass.SO, CollegeClass.find("Sophomore"), "By Long Name");
        assertEquals(CollegeClass.SO, CollegeClass.find("So."), "By Other Name");
    }

    @Test
    public void findRedshirtSophomore() {
        assertEquals(CollegeClass.REDSHIRT_SO, CollegeClass.find("REDSHIRT_SO"), "By Name");
        assertEquals(CollegeClass.REDSHIRT_SO, CollegeClass.find("Redshirt Sophomore"), "By Long Name");
        assertEquals(CollegeClass.REDSHIRT_SO, CollegeClass.find("R-So."), "By Other Name - R-So.");
        assertEquals(CollegeClass.REDSHIRT_SO, CollegeClass.find("RS So."), "By Other Name - RS So.");
        assertEquals(CollegeClass.REDSHIRT_SO, CollegeClass.find("R-Sophomore"), "By Other Name - R-Sophomore");
    }

    @Test
    public void findJunior() {
        assertEquals(CollegeClass.JR, CollegeClass.find("JR"), "By Name");
        assertEquals(CollegeClass.JR, CollegeClass.find("Junior"), "By Long Name");
        assertEquals(CollegeClass.JR, CollegeClass.find("Jr."), "By Other Name");
    }

    @Test
    public void findRedshirtJunior() {
        assertEquals(CollegeClass.REDSHIRT_JR, CollegeClass.find("REDSHIRT_JR"), "By Name");
        assertEquals(CollegeClass.REDSHIRT_JR, CollegeClass.find("Redshirt Junior"), "By Long Name");
        assertEquals(CollegeClass.REDSHIRT_JR, CollegeClass.find("R-Jr."), "By Other Name - R-Jr.");
        assertEquals(CollegeClass.REDSHIRT_JR, CollegeClass.find("RS Jr."), "By Other Name - RS Jr.");
        assertEquals(CollegeClass.REDSHIRT_JR, CollegeClass.find("R-Junior"), "By Other Name - R-Junior");
    }

    @Test
    public void findSenior() {
        assertEquals(CollegeClass.SR, CollegeClass.find("SR"), "By Name");
        assertEquals(CollegeClass.SR, CollegeClass.find("Senior"), "By Long Name");
        assertEquals(CollegeClass.SR, CollegeClass.find("Sr."), "By Other Name");
    }

    @Test
    public void findRedshirtSenior() {
        assertEquals(CollegeClass.REDSHIRT_SR, CollegeClass.find("REDSHIRT_SR"), "By Name");
        assertEquals(CollegeClass.REDSHIRT_SR, CollegeClass.find("Redshirt Senior"), "By Long Name");
        assertEquals(CollegeClass.REDSHIRT_SR, CollegeClass.find("R-Sr."), "By Other Name - R-Sr.");
        assertEquals(CollegeClass.REDSHIRT_SR, CollegeClass.find("RS Sr."), "By Other Name - RS Sr.");
        assertEquals(CollegeClass.REDSHIRT_SR, CollegeClass.find("R-Senior"), "By Other Name - R-Senior");
    }

    @Test
    public void findFifthYear() {
        assertEquals(CollegeClass.FIFTH_YR, CollegeClass.find("FIFTH_YR"), "By Name");
        assertEquals(CollegeClass.FIFTH_YR, CollegeClass.find("Fifth Year"), "By Long Name");
        assertEquals(CollegeClass.FIFTH_YR, CollegeClass.find("5th-Year Senior"), "By Other Name - 5th-Year Senior");
        assertEquals(CollegeClass.FIFTH_YR, CollegeClass.find("5th"), "By Other Name - 5th");
    }

    @Test
    public void findSixthYear() {
        assertEquals(CollegeClass.SIXTH_YR, CollegeClass.find("SIXTH_YR"), "By Name");
        assertEquals(CollegeClass.SIXTH_YR, CollegeClass.find("Sixth Year"), "By Long Name");
        assertEquals(CollegeClass.SIXTH_YR, CollegeClass.find("6th"), "By Other Name - 6th");
    }

    @Test
    public void findGraduateStudent() {
        assertEquals(CollegeClass.GR, CollegeClass.find("GR"), "By Name");
        assertEquals(CollegeClass.GR, CollegeClass.find("Graduate Student"), "By Long Name");
        assertEquals(CollegeClass.GR, CollegeClass.find("Graduate"), "By Other Name - Graduate");
        assertEquals(CollegeClass.GR, CollegeClass.find("Gr."), "By Other Name - Gr.");
    }

    @Test
    public void findRedshirt() {
        assertEquals(CollegeClass.REDSHIRT, CollegeClass.find("REDSHIRT"), "By Name");
        assertEquals(CollegeClass.REDSHIRT, CollegeClass.find("Redshirt"), "By Long Name");
        assertEquals(CollegeClass.REDSHIRT, CollegeClass.find("Rs."), "By Other Name");
    }

    @Test
    public void findWhenNotMatching() {
        assertNull(CollegeClass.find("BOGUS"));
    }
}
