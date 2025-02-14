package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz.entity.Qusetion;

public class SearchVO extends CreateReq {

	private int quizId;

	public SearchVO() {
		super();

	}

//	public SearchVO(int quizId) {
//		super();
//		this.quizId = quizId;
//	}

	public SearchVO(int quizId, String name, String description, LocalDate startDate, LocalDate endDate,
			boolean published, List<Qusetion> questionList) {
		super(name, description, startDate, endDate, published, questionList);
		this.quizId = quizId;
		// 題目編號:quiz_id
	}

	public int getQuizId() {
		return quizId;
	}

}
