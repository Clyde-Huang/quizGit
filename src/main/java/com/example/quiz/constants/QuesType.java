package com.example.quiz.constants;

public enum QuesType {
	SINGLE("單選"), MULTIPLE("多選"), TEXT("文字");

	private String type;

	private QuesType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	

}
