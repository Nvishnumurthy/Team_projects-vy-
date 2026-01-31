package com.studentcourse.enrollmentapp.service.impl;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.studentcourse.enrollmentapp.model.Course;
import com.studentcourse.enrollmentapp.model.Student;
import com.studentcourse.enrollmentapp.repository.CourseRepository;
import com.studentcourse.enrollmentapp.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

	CourseRepository courseRepository;

	public CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public Course getById(Integer id) {
		return courseRepository.findById(id).get();
	}

	@Override
	public String addCourse(Course course) {
		courseRepository.save(course);
		return "added";
	}

	@Override
	public String updateCourse(Course course) {
		courseRepository.save(course);
		return "updated";
	}

	@Override
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public String deleteCourse(Integer id) {
		courseRepository.deleteById(id);
		return "deleted";
	}

	@Override
	public Set<Student> getStudentsOfThisCourse(Integer course_id) {
		Course course = courseRepository.findById(course_id).get();
		return course.getStudents();

	}

	@Override
	public Page<Course> getPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return courseRepository.findAll(pageable);
	}

}
