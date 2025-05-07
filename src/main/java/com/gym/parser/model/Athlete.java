package com.gym.parser.model;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Athlete {
    private String firstName;
    private String lastName;
    private Integer year;
    private College college;
    private AcademicYear academicYear;
    private String homeTown;
    private State homeState;
    private Country homeCountry;
    private String club;
    private String position;
}
