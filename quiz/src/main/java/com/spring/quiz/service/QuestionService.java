package com.spring.quiz.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.spring.quiz.model.Question;

public interface QuestionService {
		
	public Question getQuestionById(Integer questionId);
	public List<Question> getAllQuestions();
	public String createQuestion(Question question);
	public String updateQuestion(Question question);
	public String deleteQuestion(Integer questionId);
	public ResponseEntity<List<Question>> getQuestionByCategory(String questionCategory);
	public List<Question> getQuestionByDifficulty(String questionDifficulty);
}
