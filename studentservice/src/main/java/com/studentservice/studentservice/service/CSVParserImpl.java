package com.studentservice.studentservice.service;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.studentservice.studentservice.model.Student;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParserImpl implements CSVParser {


    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private boolean isValidGrade(String grade) {
        return grade.matches("^[A-Z]$");
    }

    @Override
    public List<Student> readCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<Student> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader = new CSVReader(reader);
        String[] headers = csvReader.readNext();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            boolean skipRow = false;
            Student student = new Student();
            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].trim()) {
                    case "firstName":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        student.setFirstName(nextRecord[i]);
                        break;
                    case "lastName":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        student.setLastName(nextRecord[i]);
                        break;
                    case "dob":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        student.setDob(parseDate(nextRecord[i]));
                        break;
                    case "doj":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        student.setDoj(parseDate(nextRecord[i]));
                        break;
                    case "grade":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty() || (!isValidGrade(nextRecord[i].trim()))) {
                            skipRow = true;
                            break;
                        }
                        student.setGrade(nextRecord[i].trim());
                        break;
                    default:
                        break;
                }
            }
            if (!skipRow) {
                students.add(student);
            }
        }
        return students;
    }
}
