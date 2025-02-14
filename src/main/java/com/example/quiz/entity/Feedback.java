package com.example.quiz.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
@IdClass(value=FeedbackId.class) //多ID時記得加
@Entity
@Table(name = "feedback")
public class Feedback {
	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Column(name = "user_name")
	private String userName;

	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "age")
	private int age;

	@Id
	@Column(name = "ques_id")
	private int quesId;

	@Column(name = "answer")
	private String answer;
	
	@Column(name = "create_time")
	private LocalDate createTime; 

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

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDate getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDate createTime) {
		this.createTime = createTime;
	}

}
