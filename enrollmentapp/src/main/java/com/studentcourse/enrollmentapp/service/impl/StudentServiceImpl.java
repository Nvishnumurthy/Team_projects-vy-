package com.studentcourse.enrollmentapp.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.studentcourse.enrollmentapp.model.Course;
import com.studentcourse.enrollmentapp.model.Student;
import com.studentcourse.enrollmentapp.repository.CourseRepository;
import com.studentcourse.enrollmentapp.repository.StudentRepository;
import com.studentcourse.enrollmentapp.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	StudentRepository studentRepository;
	CourseRepository courseRepository;

	public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public Student getById(Integer id) {
		return studentRepository.findById(id).get();
	}

	@Override
	public String addStudent(Student student) {
		studentRepository.save(student);
		return "added";
	}

	@Override
	public String updateStudent(Student student) {
		studentRepository.save(student);
		return "updated";
	}

	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	public String deleteStudent(Integer id) {
		studentRepository.deleteById(id);
		return "deleted";
	}

	@Override
	public Student enrollStudent(Integer student_id, Integer course_id) {
		Student student = studentRepository.findById(student_id).get();
		Course course = courseRepository.findById(course_id).get();
		student.getCourses().add(course);
		return studentRepository.save(student);
	}

	@Override
	public List<Student> searchStudent(String name) {
		return studentRepository.findByNameContainingIgnoreCase(name);
	}

	@Override
	public String unenrollStudent(Integer student_id, Integer course_id) {
		Student student = studentRepository.findById(student_id).get();
		Course course = courseRepository.findById(course_id).get();
		if (student.getCourses().contains(course)) {
			student.getCourses().remove(course);
			return "Unerolled";
		} else {
			return "Student Not enrolled in this course";
		}

	}

	@Override
	public Set<Course> getCoursesOfThisStudent(Integer Student_id) {
		Student student = studentRepository.findById(Student_id).get();
		return student.getCourses();
	}

	@Override
	public Page<Student> getPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return studentRepository.findAll(pageable);
	}

	@Override
	public ResponseEntity<String> home() {
				return new ResponseEntity<String>("Welcome to Student-Enrollment Application",HttpStatus.OK);
	}

}
