package com.studentcourse.enrollmentapp.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
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
import com.studentcourse.enrollmentapp.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {

	CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/get/{course_id}")
	public Course getCourseDetails(@PathVariable("course_id") Integer course_id) {
		return courseService.getById(course_id);
	}

	@GetMapping("/getall")
	public List<Course> getAllCourseDetails() {
		return courseService.getAllCourses();
	}

	@PostMapping("/add")
	public String addCourseDetails(Course course) {
		courseService.addCourse(course);
		return "Course Details Added Successfully";
	}

	@PutMapping("/update")
	public String updateCourseDetails(Course course) {
		courseService.updateCourse(course);
		return "Course Updated Successfully";
	}

	@DeleteMapping("/delete/{course_id}")
	public String deleteCourseDetails(@PathVariable("course_id") Integer course_id) {
		courseService.deleteCourse(course_id);
		return "Deleted Curse Details Successfully";
	}

	@GetMapping("/{course_id}/students")
	public Set<Student> getStudentsOfThisCourseDetails(@PathVariable("course_id") Integer course_id) {
		return courseService.getStudentsOfThisCourse(course_id);
	}

	@GetMapping("/pages")
	public Page<Course> getCoursePaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return courseService.getPaged(page, size);
	}

}
