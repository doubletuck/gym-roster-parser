package com.gym.parser.model;

import lombok.Getter;

public enum StaffRole {

    HEAD_COACH("Head Coach"),
    ACTING_HEAD_COACH("Acting Head Coach"),
    ASSOC_HEAD_COACH("Associate Head Coach"),
    ASST_HEAD_COACH("Assistant Head Coach"),
    ASST_COACH("Assistant Coach"),
    ACTING_ASST_COACH("Acting Assistant Coach"),
    INTERIM_ASST_COACH("Interim Assistant Coach"),
    GRADUATE_ASST_COACH("Graduate Student Assistant Coach"),
    GRADUATE_COACH("Graduate Student Coach"),
    STUDENT_ASST_COACH("Student Assistant Coach"),
    STUDENT_COACH("Student Coach"),
    UNDERGRAD_ASST_COACH("Undergraduate Student Assistant Coach"),
    UNDERGRAD_COACH("Undergraduate Student Coach"),
    VOLUNTEER_ASST_COACH("Volunteer Assistant Coach"),
    VOLUNTEER_COACH("Volunteer Coach");

    @Getter
    private final String longName;

    StaffRole(String longName) {
        this.longName = longName;
    }
}
