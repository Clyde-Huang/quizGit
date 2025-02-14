package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;
import com.example.quiz.entity.Qusetion;

public class CreateReq { // Req只要生成get set即可(?) 建構方法有postman
	//問卷
	private String name;

	private String description;

	private LocalDate startDate;

	private LocalDate endDate;

	private boolean published;
	//問卷
	
	private List<Qusetion> questionList;  //題目

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<Qusetion> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Qusetion> questionList) {
		this.questionList = questionList;
	}

	public CreateReq() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public CreateReq(String name, String description, LocalDate startDate, LocalDate endDate, boolean published,
			List<Qusetion> questionList) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
		this.questionList = questionList;
	}

}
