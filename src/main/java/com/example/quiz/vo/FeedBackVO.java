package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

public class FeedBackVO {
	private int quizId;
	private String quizName;
	private String description;
	private String userName;
	private String email;
	private int age;
	private LocalDate createDate;
	private List<OptionAnswer> optionAnswerList;
	// 去掉 quesId , quesName, answer(用集合包多個問題回答),
	
	
	
	public int getQuizId() {
		return quizId;
	}
	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
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
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public List<OptionAnswer> getOptionAnswerList() {
		return optionAnswerList;
	}
	public void setOptionAnswerList(List<OptionAnswer> optionAnswerList) {
		this.optionAnswerList = optionAnswerList;
	}
	public FeedBackVO(int quizId, String quizName, String description, String userName, String email, int age,
			LocalDate createDate, List<OptionAnswer> optionAnswerList) {
		super();
		this.quizId = quizId;
		this.quizName = quizName;
		this.description = description;
		this.userName = userName;
		this.email = email;
		this.age = age;
		this.createDate = createDate;
		this.optionAnswerList = optionAnswerList;
	}
	public FeedBackVO() {
		super();
		// TODO 自動產生的建構子 Stub
	}


	
}
