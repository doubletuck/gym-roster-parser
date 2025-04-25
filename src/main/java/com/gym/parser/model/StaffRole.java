package com.gym.parser.model;

import lombok.Getter;

public enum StaffRole {

    ASST_COACH("Assistant Coach"),
    ASSOC_HEAD_COACH("Associate Head Coach"),
    HEAD_COACH("Head Coach"),
    HEAD_COACH_ASST("Assistant to the Head Coach"),
    HEAD_TEAM_MGR("Head Team Manager"),
    OPERATIONS_DIR("Director of Operations"),
    STUDENT_ASST_COACH("Student Assistant Coach"),
    STUDENT_COACH("Student Coach"),
    UNDERGRAD_ASST_COACH("Undergraduate Assistant Coach"),
    UNDERGRAD_STUDENT_COACH("Undergraduate Student Coach"),
    VOLUNTEER_ASST_COACH("Volunteer Assistant Coach"),
    VOLUNTEER_COACH("Volunteer Coach");

    @Getter
    private final String longName;

    StaffRole(String longName) {
        this.longName = longName;
    }
}
