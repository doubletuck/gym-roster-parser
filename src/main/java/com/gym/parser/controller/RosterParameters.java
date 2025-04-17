package com.gym.parser.controller;

import com.gym.parser.model.College;
import lombok.Data;

@Data
public class RosterParameters {

    private College college;
    private final Integer year;
}
