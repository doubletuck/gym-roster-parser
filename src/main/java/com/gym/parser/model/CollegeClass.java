package com.gym.parser.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum CollegeClass {

    FR("Freshman", "Fr."),
    REDSHIRT_FR("Redshirt Freshman", "R-Fr."),
    SO("Sophomore", "So."),
    REDSHIRT_SO("Redshirt Sophomore", "R-So."),
    JR("Junior", "Jr."),
    REDSHIRT_JR("Redshirt Junior", "R-Jr."),
    SR("Senior", "Sr."),
    REDSHIRT_SR("Redshirt Senior", "R-Sr."),
    FIFTH_SR("5th-Year Senior"),
    GR("Graduate Student", "Graduate", "Gr."),
    XX("Undetected");

    private final String collegeClassName;
    private final String[] otherNames;

    CollegeClass(String collegeClassName, String... otherNames) {
        this.collegeClassName = collegeClassName;
        this.otherNames = otherNames;
    }

    /**
     * Returns the college class enum that matches the given text.
     *
     * @param text The college class code or name.
     * @return The CollegeClass enum that matches the given text or null if no
     * matches are found.
     */
    public static CollegeClass find(String text) {
        if (text != null && !text.isBlank()) {
            for (CollegeClass collegeClass : CollegeClass.values()) {
                if (collegeClass.name().equalsIgnoreCase(text) ||
                        collegeClass.collegeClassName.equalsIgnoreCase(text) ||
                        StringUtils.equalsAnyIgnoreCase(text, collegeClass.otherNames)) {
                    return collegeClass;
                }
            }
        }
        return XX;
    }
}
