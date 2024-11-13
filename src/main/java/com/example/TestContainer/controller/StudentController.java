package com.example.TestContainer.controller;


import com.example.TestContainer.entities.Student;
import com.example.TestContainer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Endpoint to create a new student
    @PostMapping("/save")
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // Endpoint to get student by ID
    @GetMapping("/get")
    public List<Student> getAll() {
        return studentService.getAll();
    }
}