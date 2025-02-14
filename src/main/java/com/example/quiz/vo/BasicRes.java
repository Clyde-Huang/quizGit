package com.example.quiz.vo;

public class BasicRes {

	private int Code;
	
	private String message;

	public BasicRes() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public BasicRes(int code, String message) {
		super();
		Code = code;
		this.message = message;
	}

	public int getCode() {
		return Code;
	}

	public String getMessage() {
		return message;
	}
	
	
}
