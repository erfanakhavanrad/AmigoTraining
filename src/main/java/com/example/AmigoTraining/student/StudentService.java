package com.example.AmigoTraining.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

//@Component
@Service
public class StudentService {


  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getStudents() {
//    return List.of("Hello World!", "my");
//    return List.of(new Student(1L, "Mery", "mery@gmail.com"
//        , LocalDate.of(2000, Month.JANUARY, 5),
//        5));
    return studentRepository.findAll();
  }

  public void addNewStudent(Student student) {
    Optional<Student> studentOptional = studentRepository
        .findStudentByEmail(student.getEmail());
    if (studentOptional.isPresent()) {
      throw new IllegalStateException("Email already exists");
    }
    studentRepository.save(student);
  }

  public void deleteStudent(Long studentId) {
    studentRepository.findById(studentId);
    boolean exists = studentRepository.existsById(studentId);
    if (!exists) {
      throw new IllegalStateException("student with id " + studentId + " does not exists");
    }
    studentRepository.deleteById(studentId);
  }

  @Transactional
  public void updateStudent(Long studentId,
      String name,
      String email) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException(
            "student with id " + studentId + " not found."
        ));

    if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
      student.setName(name);
    }

    if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
      Optional<Student> studentOptional = studentRepository
          .findStudentByEmail(email);
      if (studentOptional.isPresent()) {
        throw new IllegalStateException("Email taken");
      }
      student.setEmail(email);
    }

  }
}
