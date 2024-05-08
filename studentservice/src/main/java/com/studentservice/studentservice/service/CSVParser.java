package com.studentservice.studentservice.service;

import com.opencsv.exceptions.CsvValidationException;
import com.studentservice.studentservice.model.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CSVParser {

    List<Student> readCsv(MultipartFile file) throws IOException, CsvValidationException;
}
