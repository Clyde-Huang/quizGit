package com.example.quiz.vo;

import java.util.List;
import java.util.Optional;

import com.example.quiz.entity.Quiz;

public class QuizSearchRes extends BasicRes { 
	       
private List<Quiz>quizList;

public QuizSearchRes(int i, String string, Optional<Quiz> res) {
	super();
	// TODO 自動產生的建構子 Stub //hfhxhf
}

public QuizSearchRes(int code, String message) {
	super(code, message);
	// TODO 自動產生的建構子 Stub
}

public QuizSearchRes(int code, String message,List<Quiz> quizList) {
	super(code, message);
	this.quizList = quizList;
}

public QuizSearchRes(int code, String message, Quiz quiz) {
	// TODO 自動產生的建構子 Stub //sdfege
}

public List<Quiz> getQuizList() {
	return quizList;
}

}
