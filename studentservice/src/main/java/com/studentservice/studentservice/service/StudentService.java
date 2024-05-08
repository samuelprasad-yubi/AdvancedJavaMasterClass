package com.studentservice.studentservice.service;

import com.opencsv.exceptions.CsvValidationException;
import com.studentservice.studentservice.dto.ResponseDto;
import com.studentservice.studentservice.model.Student;
import com.studentservice.studentservice.model.StudentFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentService {

    String saveStudentData(MultipartFile file) throws CsvValidationException, IOException;

    ResponseDto fetchStudent(Integer id);

    String modifyStudent(Student studentData);

    ResponseDto searchStudent(Integer pageNo, Integer pageSize, StudentFilterDTO filterDTO);

    ResponseEntity<byte[]> generateCSV(StudentFilterDTO filterDTO) throws IOException;

    ResponseEntity<byte[]> sampleCSV() throws IOException;

}
