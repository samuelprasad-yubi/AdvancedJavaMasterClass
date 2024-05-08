package com.studentservice.studentservice.constants;


import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ConstantService {

    @Getter
    private static final String AUTH_ENDPOINT = "/user/is-authenticated";

    @Getter
    private static final String AUTHORIZATION = "Authorization";

    @Getter
    private static final String STUDENT_FIRSTNAME = "firstname";

    @Getter
    private static final String STUDENT_LASTNAME = "lastname";

    @Getter
    private static final String DOB = "dob";

    @Getter
    private static final String DOJ = "doj";

    @Getter
    private static final String GRADE = "grade";

}
