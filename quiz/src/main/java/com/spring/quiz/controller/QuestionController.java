package com.spring.quiz.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.quiz.model.Question;
import com.spring.quiz.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {
	
		QuestionService questionService;
		public QuestionController(QuestionService questionService) {
			this.questionService=questionService;
		}
		
	@GetMapping("/getquestion/{id}")
	public Question getQuestionDetails(@PathVariable("id") Integer id) {
		return questionService.getQuestionById(id);
	}
	
	@GetMapping("/allquestions")
	public List<Question> getAllQuestionDetails() {
		return questionService.getAllQuestions();
	}
	
	@PostMapping("/createquestion")
	public String createNewQuestion(@RequestBody Question question) {
		questionService.createQuestion(question);
		return "Created New Question Successfully";
	}
	
	@PutMapping("/updatequestion")
	public String updateExistingQuestion(@RequestBody Question question) {
		questionService.updateQuestion(question);
		return"Updated the Existing Question Successfully";
	}
	
	@DeleteMapping("/deletequestion/{id}")
	public String deleteExistingQuestion(@PathVariable("id") Integer id) {
		questionService.deleteQuestion(id);
		return "Deleted the question Successfully";
	}
	@GetMapping("/category/{category}")
	public ResponseEntity<List<Question> > getQuestionByCategoryDetails
						(@PathVariable("category") String category){
		return questionService.getQuestionByCategory(category);
	}
	
	@GetMapping("/difficulty/{difficulty}")
	public List<Question> getQuestionByDifficultyDetails
						(@PathVariable("difficulty") String difficulty){
		return questionService.getQuestionByDifficulty(difficulty);
	}
}
