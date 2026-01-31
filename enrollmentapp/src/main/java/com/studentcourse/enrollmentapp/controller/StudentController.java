package com.studentcourse.enrollmentapp.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentcourse.enrollmentapp.model.Course;
import com.studentcourse.enrollmentapp.model.Student;
import com.studentcourse.enrollmentapp.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

	StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/home")
	public ResponseEntity<String> getHome(){
		return studentService.home();
	}
	
	@GetMapping("/get/{student_id}")
	@PreAuthorize("hasRole('USER')")
	public Student getStudentDetails(@PathVariable("student_id") Integer student_id) {
		return studentService.getById(student_id);
	}

	@GetMapping("/getall")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Student> getAllStudentDetails() {
		return studentService.getAllStudents();
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")

	public String getUpdatedStudentDetails(Student student) {
		studentService.updateStudent(student);
		return "Student Updated Successfully";
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")

	public String addNewStudent(Student student) {
		studentService.addStudent(student);
		return "New Student Added Successfully";
	}

	@DeleteMapping("/delete/{student_id}")
	@PreAuthorize("hasRole('ADMIN')")

	public String deleteStudentDetails(@PathVariable("student_id") Integer student_id) {
		studentService.deleteStudent(student_id);
		return "Student Details Deleted Successfully";
	}

	@PostMapping("/{student_id}/enroll/{course_id}")
	public Student enrollStudentDeails(@PathVariable("student_id") Integer student_id,
			@PathVariable("course_id") Integer course_id) {
		return studentService.enrollStudent(student_id, course_id);
	}

	@GetMapping("/search")
	public List<Student> searchTheStudent(@RequestParam String name) {
		return studentService.searchStudent(name);
	}

	@DeleteMapping("/{student_id}/unenroll/{course_id}")
	public String unenrollTheStudent(@PathVariable("student_id") Integer student_id,
			@PathVariable("course_id") Integer course_id) {
		return studentService.unenrollStudent(student_id, course_id);

	}

	@GetMapping("/{student_id}/courses")
	public Set<Course> getCoursesOfThisStudentDetails(@PathVariable("student_id") Integer student_id) {
		return studentService.getCoursesOfThisStudent(student_id);
	}

	@GetMapping("/paged")
	public Page<Student> getStudentsPaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return studentService.getPaged(page, size);
	}

}
