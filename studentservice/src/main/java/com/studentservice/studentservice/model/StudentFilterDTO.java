package com.studentservice.studentservice.model;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private LocalDate doj;

    @Pattern(regexp = "[A-Z]", message = "Grade must be in the format M1 to M6")
    private String grade;

}

