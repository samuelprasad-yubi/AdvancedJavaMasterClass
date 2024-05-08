package com.studentservice.studentservice.repository;

import com.studentservice.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findById(Integer studentId);
}
