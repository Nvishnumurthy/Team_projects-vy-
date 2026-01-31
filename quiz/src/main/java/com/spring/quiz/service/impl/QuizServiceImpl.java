package com.spring.quiz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.quiz.model.Question;
import com.spring.quiz.model.QuestionWrapper;
import com.spring.quiz.model.Quiz;
import com.spring.quiz.model.Response;
import com.spring.quiz.repository.QuestionRepository;
import com.spring.quiz.repository.QuizRepository;
import com.spring.quiz.service.QuizService;

@Service
public class QuizServiceImpl implements QuizService{
	
	QuizRepository quizRepository;
	QuestionRepository questionRepository;
	
	
    public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }
    
	@Override
	public ResponseEntity<String> createQuiz(String category, Integer numq, String title) {
		List<Question> questions = questionRepository.findRandomQuestionsByCategory(category, numq);
		Quiz q = new Quiz();
		q.setTitle( title);
		q.setQuestions(questions); 
		quizRepository.save(q);
		return new ResponseEntity<>("Quiz Created",HttpStatus.CREATED);
	}
	@Override
	public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
			Optional<Quiz> q2 = quizRepository.findById(id);
			List<Question> questionsFromDb = q2.get().getQuestions();
			List<QuestionWrapper> questionForUser = new ArrayList<>();
			for(Question q: questionsFromDb) {
				QuestionWrapper	qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(),
						q.getOption1(), q.getOption2(), q.getOption3());
				questionForUser.add(qw);
						
			}
			return new ResponseEntity<>(questionForUser,HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getResult(Integer id,List<Response> responses){
				Optional<Quiz> q3 = quizRepository.findById(id);
				List<Question> fromQuestions = q3.get().getQuestions();
				int right = 0;
			    for (Response r : responses) {
			        for (Question q : fromQuestions) {
			            if (q.getId().equals(r.getId()) &&
			                q.getRightAnswer().equalsIgnoreCase(r.getResponse())) {
			                right++;
			                break;
			            }
			        }
			    }
			    int total = responses.size();
			    int percentage =(right*100)/total;
			    String feedBack = (percentage >80)?"Excellent"
			    				:(percentage >=50)?"Good"
			    				:"Need Improvement";
			    Map<String, Object>result = new HashMap<String, Object>();
			    result.put("Score", right);
			    result.put("Total Questions", total);
			    result.put("Percentage", percentage);
			    result.put("FeedBack", feedBack);
			return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@Override
	public List<Quiz> getAllQuiz() {
				return quizRepository.findAll();
	}

	@Override
	public Object getQuizInfo(Integer id) {
		Optional<Quiz> q4 = quizRepository.findById(id);
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("Quiz-Title", q4.get().getTitle());
		info.put("Questions-Count", q4.get().getQuestions().size());
		return info;
	}
}
