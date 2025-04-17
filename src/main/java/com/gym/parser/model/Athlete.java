package com.gym.parser.model;

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
    private CollegeClass collegeClass;
    private String homeTown;
    private State homeState;
    private Country homeCountry;
    private String club;
    private String position;
}
