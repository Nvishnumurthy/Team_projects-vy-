package com.spring.quiz.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.spring.quiz.model.QuestionWrapper;
import com.spring.quiz.model.Quiz;
import com.spring.quiz.model.Response;

public interface QuizService {
		
	public ResponseEntity<String> createQuiz(String category,Integer numq,String title);

	public  ResponseEntity<List<QuestionWrapper>>   getQuiz(Integer id);
	
	public ResponseEntity<Object> getResult(Integer id,List<Response> responses);

	public List<Quiz> getAllQuiz();
	
	public Object getQuizInfo(Integer id);
} 
		