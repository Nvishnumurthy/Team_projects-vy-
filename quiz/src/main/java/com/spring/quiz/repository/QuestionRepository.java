package com.spring.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.quiz.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> 
{
		List<Question> findByCategory(String category);
		List<Question> findByDifficulty(String difficulty);
		
		@Query(value = "SELECT * FROM questions q WHERE q.category=:category ORDER BY RAND() LIMIT :numq", nativeQuery=true)
		List<Question> findRandomQuestionsByCategory
						(String category,Integer numq);
}
