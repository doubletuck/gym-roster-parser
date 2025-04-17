package com.gym.parser.controller;

import com.gym.parser.model.College;
import lombok.Data;

@Data
public class ExportParameters {

    private College college;
    private Integer year;
    private String fileName;
}
