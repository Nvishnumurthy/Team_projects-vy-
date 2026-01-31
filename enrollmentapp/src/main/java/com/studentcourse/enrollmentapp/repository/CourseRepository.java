package com.studentcourse.enrollmentapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentcourse.enrollmentapp.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
