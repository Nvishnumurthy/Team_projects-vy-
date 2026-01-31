package com.spring.quiz.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.quiz.model.Question;
import com.spring.quiz.repository.QuestionRepository;
import com.spring.quiz.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
		
	QuestionRepository questionRepository;
	public QuestionServiceImpl(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	@Override
	public Question getQuestionById(Integer questionId) {
		return questionRepository.findById(questionId).get();
	}
	
	@Override
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}
	
	@Override
	public String createQuestion(Question question) {
		 questionRepository.save(question);
		 return "Created";
	}
	@Override
	public String updateQuestion(Question question) {
		 questionRepository.save(question);
		 return "updated";
	}
	@Override
	public String deleteQuestion(Integer questionId) {
		  questionRepository.deleteById(questionId);
		 return "Deleted";
	}
	@Override
	public ResponseEntity<List<Question>> getQuestionByCategory(String questionCategory) {
		try{
			return new ResponseEntity<>(questionRepository.findByCategory(questionCategory),
					HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(questionRepository.findByCategory(questionCategory),
				HttpStatus.BAD_REQUEST);
	}
	@Override
	public List<Question> getQuestionByDifficulty(String questionDifficulty) {
		return questionRepository.findByDifficulty(questionDifficulty);
	}

}
