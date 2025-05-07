package com.gym.parser.controller;

import com.doubletuck.gym.common.model.College;
import lombok.Data;

@Data
public class RosterParameters {

    private College college;
    private final Integer year;
}
