package com.spring.quiz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.quiz.model.QuestionWrapper;
import com.spring.quiz.model.Quiz;
import com.spring.quiz.model.Response;
import com.spring.quiz.service.QuizService;

@RestController
@RequestMapping("/quiz")
public class QuizController {
			
	QuizService quizService;
	public QuizController(QuizService quizService) {
		this.quizService=quizService;
	}
	
	@GetMapping("/getall")
	public List<Quiz> getAllExistingQuiz(){
		return quizService.getAllQuiz();
	}
	
	@GetMapping("/get/info/{id}")
	public Object getExistingQuizInfo(@PathVariable("id") Integer id) {
		return quizService.getQuizInfo(id);
	}
	 
	@PostMapping("/create")
	public ResponseEntity<String>  createNewQuiz(@RequestParam String category,
			@RequestParam Integer numq,@RequestParam String title) {
		quizService.createQuiz(category, numq, title);
		return new ResponseEntity<>("Quiz Created Successfully",HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<QuestionWrapper>> getExistingQuiz( @PathVariable("id")  Integer id) {
		return quizService.getQuiz(id);
	}
	
	@PostMapping("/submit/{id}")
	public ResponseEntity<Object> getQuizResult(@PathVariable("id") Integer id,
					@RequestBody List<Response> responses){
		 return quizService.getResult(id,responses);
	}
	
}
