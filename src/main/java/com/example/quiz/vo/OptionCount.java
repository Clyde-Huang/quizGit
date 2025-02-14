package com.example.quiz.vo;

public class OptionCount {
	private int quseId;
	private String option;
	private int count=0;

	public void incrementCount() {
        this.count++;
    }
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getQuseId() {
		return quseId;
	}

	public void setQuseId(int quseId) {
		this.quseId = quseId;
	}

}
