package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum CollegeClass {

    FR("Freshman", "Fr."),
    REDSHIRT_FR("Redshirt Freshman", "R-Fr.", "RS Fr.", "R-Freshman"),
    SO("Sophomore", "So."),
    REDSHIRT_SO("Redshirt Sophomore", "R-So.", "RS So.", "R-Sophomore"),
    JR("Junior", "Jr."),
    REDSHIRT_JR("Redshirt Junior", "R-Jr.", "RS Jr.", "R-Junior"),
    SR("Senior", "Sr."),
    REDSHIRT_SR("Redshirt Senior", "R-Sr.", "RS Sr.", "R-Senior"),
    FIFTH_SR("Fifth Year", "5th-Year Senior"),
    GR("Graduate Student", "Graduate", "Gr.");

    private final String longName;
    private final String[] otherNames;

    CollegeClass(String longName, String... otherNames) {
        this.longName = longName;
        this.otherNames = otherNames;
    }

    /**
     * Returns the college class enum that matches the given text.
     *
     * @param   text The college class code or name.
     * @return  The CollegeClass enum that matches the given text or
     *          null if no matches are found.
     */
    public static CollegeClass find(String text) {
        if (text != null && !text.isBlank()) {
            text = text.trim();
            for (CollegeClass collegeClass : values()) {
                if (collegeClass.name().equalsIgnoreCase(text) ||
                        collegeClass.longName.equalsIgnoreCase(text) ||
                        StringUtils.equalsAnyIgnoreCase(text, collegeClass.otherNames)) {
                    return collegeClass;
                }
            }
        }
        return null;
    }
}
