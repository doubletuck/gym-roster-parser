package com.gym.parser.controller;

import com.doubletuck.gym.common.model.College;
import lombok.Data;

@Data
public class ExportParameters {

    private College college;
    private Integer year;
    private String fileName;
    private boolean overwriteExistingFile;
}
