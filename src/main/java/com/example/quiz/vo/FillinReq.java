package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FillinReq {

	private int quizId;

	private String userName;

	private String email;

	private int age;

	private Map<Integer, List<String>> quesIdAnswerMap; // 問題編號(quseId) 回答(answer) 1對1

	// private int quesId; //第幾個問題 被map取代

	// private String answer; //第幾個問題的答案 被map取代

	private LocalDate createTime=LocalDate.now(); //預設now()

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Map<Integer, List<String>> getQuesIdAnswerMap() {
		return quesIdAnswerMap;
	}

	public void setQuesIdAnswerMap(Map<Integer, List<String>> quesIdAnswerMap) {
		this.quesIdAnswerMap = quesIdAnswerMap;
	}

	public LocalDate getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDate createTime) {
		this.createTime = createTime;
	}

}
