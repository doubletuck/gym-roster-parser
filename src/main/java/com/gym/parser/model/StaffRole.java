package com.gym.parser.model;

import lombok.Getter;

public enum StaffRole {

    ASST_COACH("Assistant Coach"),
    ASSOC_HEAD_COACH("Associate Head Coach"),
    OPERATIONS_DIR("Director of Operations"),
    HEAD_COACH("Head Coach"),
    HEAD_TEAM_MGR("Head Team Manager"),
    UNDERGRAD_ASST_COACH("Undergraduate Assistant Coach"),
    VOLUNTEER_ASST_COACH("Volunteer Assistant Coach");

    @Getter
    private final String longName;

    StaffRole(String longName) {
        this.longName = longName;
    }
}
