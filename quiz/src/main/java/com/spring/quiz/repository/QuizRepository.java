package com.spring.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quiz.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

} 
