package com.studentcourse.enrollmentapp.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.studentcourse.enrollmentapp.model.Course;
import com.studentcourse.enrollmentapp.model.Student;

public interface StudentService {

	public Student getById(Integer id);

	public String addStudent(Student student);

	public String updateStudent(Student student);

	public List<Student> getAllStudents();

	public String deleteStudent(Integer id);

	public Student enrollStudent(Integer student_id, Integer course_id);

	public List<Student> searchStudent(String name);

	public String unenrollStudent(Integer student_id, Integer course_id);

	public Set<Course> getCoursesOfThisStudent(Integer Student_id);

	public Page<Student> getPaged(int page, int size);
	
	public ResponseEntity<String> home();
}
