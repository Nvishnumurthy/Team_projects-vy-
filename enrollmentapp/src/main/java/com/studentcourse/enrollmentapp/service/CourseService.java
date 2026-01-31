package com.studentcourse.enrollmentapp.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.studentcourse.enrollmentapp.model.Course;
import com.studentcourse.enrollmentapp.model.Student;

public interface CourseService {

	public Course getById(Integer id);

	public String addCourse(Course course);

	public String updateCourse(Course course);

	public List<Course> getAllCourses();

	public String deleteCourse(Integer id);

	public Set<Student> getStudentsOfThisCourse(Integer course_id);

	public Page<Course> getPaged(int page, int size);

}
