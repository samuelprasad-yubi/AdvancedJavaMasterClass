package com.studentservice.studentservice.controller;


import com.opencsv.exceptions.CsvValidationException;
import com.studentservice.studentservice.dto.ResponseDto;
import com.studentservice.studentservice.model.Student;
import com.studentservice.studentservice.model.StudentFilterDTO;
import com.studentservice.studentservice.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadStudentData(@RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        return ResponseEntity.ok().body(studentService.saveStudentData(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> fetchStudentData(@PathVariable Integer id) {
        ResponseDto response = studentService.fetchStudent(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<String> modifyStudentData(@Valid @RequestBody Student studentData) {
        String response = studentService.modifyStudent(studentData);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseDto> searchStudentData(@RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize, @Valid @RequestBody StudentFilterDTO studentFilterDTO) {
        ResponseDto filteredResponse = studentService.searchStudent(pageNo, pageSize, studentFilterDTO);
        return ResponseEntity.ok().body(filteredResponse);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportToCSV(@Valid @RequestBody StudentFilterDTO studentFilterDTO) throws IOException {
        return studentService.generateCSV(studentFilterDTO);
    }

    @PostMapping("/sampleCSV")
    public ResponseEntity<byte[]> sampleCSV() throws IOException {
        return studentService.sampleCSV();
    }

}



