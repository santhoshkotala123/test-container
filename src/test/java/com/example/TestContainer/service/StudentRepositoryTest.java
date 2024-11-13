package com.example.TestContainer.service;

import com.example.TestContainer.entities.Student;
import com.example.TestContainer.repository.StudentRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
public class StudentRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testSaveStudent() {

        Student student = new Student();
        student.setId(1L);
        student.setName("Santhosh");
        student.setStandard("10");
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository.findByName("Santhosh");

        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getStandard()).isEqualTo("10");
    }

    @Test
    void testFindAllStudents() {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Santhosh");
        student1.setStandard("8");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Swaraj");
        student2.setStandard("9");

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findAll();

        assertThat(students).hasSize(2);
        assertThat(students).extracting(Student::getName).contains("Santhosh", "Swaraj");
        assertThat(students).extracting(Student::getStandard).contains("8", "9");
    }
}
