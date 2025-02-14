package com.example.quiz.vo;

import java.time.LocalDate;


//用於多張表時的資料型態撈回來後裝載容器
public class FeedBackDto {
	// 合併3個表(跨表)
	private String quizName;
	private String description;
	private String userName;
	private String email;
	private int age;
	private String answer;
	private LocalDate createDate;
	private int quesId;
	private String quesName;

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public void setQuesName(String quesName) {
		this.quesName = quesName;
	}

	public FeedBackDto() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public FeedBackDto(String quizName, String description, String userName, String email, int age, String answer,
			LocalDate createDate, int quesId, String quesName) {
		super();
		this.quizName = quizName;
		this.description = description;
		this.userName = userName;
		this.email = email;
		this.age = age;
		this.answer = answer;
		this.createDate = createDate;
		this.quesId = quesId;
		this.quesName = quesName;
	}

}
