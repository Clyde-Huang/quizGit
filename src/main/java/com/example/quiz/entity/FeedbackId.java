package com.example.quiz.entity;

import java.io.Serializable;

@SuppressWarnings("serial") // 多PK要多此設定
public class FeedbackId implements Serializable {

	private int quizId;
	
	private String email;
	
	private int quesId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public FeedbackId(int quizId, String email, int quesId) {
		super();
		this.quizId = quizId;
		this.email = email;
		this.quesId = quesId;
	}

	public FeedbackId() {
		super();
		// TODO 自動產生的建構子 Stub
	}

}
