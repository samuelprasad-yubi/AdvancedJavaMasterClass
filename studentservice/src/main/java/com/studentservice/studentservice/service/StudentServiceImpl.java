package com.studentservice.studentservice.service;


import com.opencsv.exceptions.CsvValidationException;
import com.studentservice.studentservice.dto.ResponseDto;
import com.studentservice.studentservice.model.Student;
import com.studentservice.studentservice.model.StudentFilterDTO;
import com.studentservice.studentservice.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    public String saveStudentData(MultipartFile file) throws CsvValidationException, IOException {
        List<Student> studentList = csvParser.readCsv(file);
        studentRepository.saveAll(studentList);
        return "Successfully uploaded";
    }

    @Override
    public ResponseDto fetchStudent(Integer id) {
        Optional<Student> fetchedStudent = studentRepository.findById(id);
        if (fetchedStudent.isPresent()) {
            return ResponseDto.builder().students(List.of(fetchedStudent.get())).build();
        } else {
            throw new RuntimeException("Student details not found");
        }
    }

    @Override
    public String modifyStudent(Student studentData) {
        Optional<Student> fetchedStudent = studentRepository.findById(studentData.getId());
        if (fetchedStudent.isPresent()) {
            studentData.setId(fetchedStudent.get().getId());
            studentRepository.save(studentData);
            return "updated successfully";
        } else {
            throw new RuntimeException("Student details not found");
        }
    }

    @Override
    public ResponseDto searchStudent(Integer pageNo, Integer pageSize, StudentFilterDTO filterDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        Root<Student> root = criteriaQuery.from(Student.class);
        Predicate predicate = criteriaBuilder.conjunction();
        if (filterDTO.getId() != null) {
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), filterDTO.getId());
            predicate = criteriaBuilder.and(predicate, idPredicate);
        }
        if (filterDTO.getFirstName() != null && !filterDTO.getFirstName().isBlank()) {
            String firstName = filterDTO.getFirstName();
            if (firstName.charAt(firstName.length() - 1) == '*') {
                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), firstName.substring(0, firstName.length() - 1) + "%");
                predicate = criteriaBuilder.and(predicate, firstNamePredicate);
            } else {
                Predicate firstNamePredicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("firstName")), firstName);
                predicate = criteriaBuilder.and(predicate, firstNamePredicate);
            }
        }
        if (filterDTO.getLastName() != null && !filterDTO.getLastName().isBlank()) {
            String lastName = filterDTO.getLastName();
            if (lastName.charAt(lastName.length() - 1) == '*') {
                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), lastName.substring(0, lastName.length() - 1) + "%");
                predicate = criteriaBuilder.and(predicate, firstNamePredicate);
            } else {
                Predicate firstNamePredicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("lastName")), lastName);
                predicate = criteriaBuilder.and(predicate, firstNamePredicate);
            }
        }
        if (filterDTO.getGrade() != null && !filterDTO.getGrade().isBlank()) {
            Predicate gradePredicate = criteriaBuilder.equal(root.get("grade"), filterDTO.getGrade());
            predicate = criteriaBuilder.and(predicate, gradePredicate);
        }
        if (filterDTO.getDoj() != null) {
            Predicate dojPredicate = criteriaBuilder.equal(root.get("doj"), filterDTO.getDoj());
            predicate = criteriaBuilder.and(predicate, dojPredicate);
        }
        if (filterDTO.getDob() != null) {
            Predicate dobPredicate = criteriaBuilder.equal(root.get("dob"), filterDTO.getDob());
            predicate = criteriaBuilder.and(predicate, dobPredicate);
        }
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
        TypedQuery<Student> query = entityManager.createQuery(criteriaQuery);
        if (pageNo != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageSize);
            Integer total = query.getResultList().size();
            List<Student> resultList = query.getResultList();
            return ResponseDto.builder().totalRecords(total).students(resultList).build();
        } else {
            List<Student> result = query.getResultList();
            return ResponseDto.builder().totalRecords(result.size()).students(result).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> generateCSV(StudentFilterDTO filterDTO) throws IOException {
        List<Student> students = searchStudent(null, null, filterDTO).getStudents();
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(getHeaders()));
        for (Student student : students) {
            csvPrinter.printRecord(student.getId(), student.getFirstName(), student.getLastName(), student.getDob(), student.getDoj(), student.getGrade());
        }
        csvPrinter.flush();
        csvPrinter.close();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Students_Output.csv");
        return ResponseEntity.ok().headers(headers).body(writer.toString().getBytes());
    }

    private String[] getHeaders() {
        Field[] fields = Student.class.getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName();
        }
        return headers;
    }

    @Override
    public ResponseEntity<byte[]> sampleCSV() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(Arrays.copyOfRange(getHeaders(), 1, getHeaders().length)));
        csvPrinter.flush();
        csvPrinter.close();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Student_Sample.csv");
        return ResponseEntity.ok().headers(headers).body(writer.toString().getBytes());
    }
}
