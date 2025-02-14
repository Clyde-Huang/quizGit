package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz.entity.Qusetion;

public class SearchRes extends BasicRes {

	private List<SearchVO> searchList;

	// 這樣建構就可多筆
	public SearchRes() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public SearchRes(int code, String message) {
		super(code, message);
		// TODO 自動產生的建構子 Stub
	}

	public SearchRes(int code, String message,List<SearchVO> searchList) {
		super(code, message);
		this.searchList = searchList;
	}
 
	
	//get
	public List<SearchVO> getSearchList() {
		return searchList;
	}
	
}
