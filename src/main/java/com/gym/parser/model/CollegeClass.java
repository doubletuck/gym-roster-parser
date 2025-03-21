package com.gym.parser.model;

import lombok.Getter;

@Getter
public enum CollegeClass {

    FR("Freshman"),
    SO("Sophomore"),
    JR("Junior"),
    SR("Senior"),
    SRFIFTH("5th-Year Senior"),
    GR("Graduate Student"),
    XX("Undetected");

    private final String collegeClassName;

    CollegeClass(String collegeClassName) {
        this.collegeClassName = collegeClassName;
    }

    /**
     * Returns the college class enum that matches the given text.
     *
     * @param text The college class code or name.
     * @return The CollegeClass enum that matches the given text or null if no
     * matches are found.
     */
    public static CollegeClass find(String text) {
        for (CollegeClass collegeClass : CollegeClass.values()) {
            if (collegeClass.name().equalsIgnoreCase(text) ||
                    collegeClass.collegeClassName.equalsIgnoreCase(text)) {
                return collegeClass;
            }
        }
        return XX;
    }
}
