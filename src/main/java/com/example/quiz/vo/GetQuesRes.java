package com.example.quiz.vo;

import java.util.List;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;

import com.example.quiz.entity.Qusetion;

public class GetQuesRes extends BasicRes{
private List<Qusetion> quesList;

public List<Qusetion> getQuesList() {
	return quesList;
}

public void setQuesList(List<Qusetion> quesList) {
	this.quesList = quesList;
}

public GetQuesRes(int code, String message,List<Qusetion> res) {
	super(code, message);
	this.quesList = res;
}

public GetQuesRes() {
	super();
	// TODO 自動產生的建構子 Stub
}

public GetQuesRes(int code, String message) {
	super(code, message);
	// TODO 自動產生的建構子 Stub
}

}
