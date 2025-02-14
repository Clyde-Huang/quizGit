package com.example.quiz.vo;

public class UpdateReq extends CreateReq { //因為除了id其他都類似
	
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	
	

}
