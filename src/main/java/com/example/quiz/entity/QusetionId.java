package com.example.quiz.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QusetionId  implements Serializable{
	// 雙PK要多此設定
	private int quesId; 
	private int quizId;
	public QusetionId() {
		super();
		// TODO 自動產生的建構子 Stub
	}
	public QusetionId(int quesId, int quizId) {
		super();
		this.quesId = quesId;
		this.quizId = quizId;
	}
	public int getQuesId() {
		return quesId;
	}
	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}
	public int getQuizId() {
		return quizId;
	}
	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	
	
	
	
}
