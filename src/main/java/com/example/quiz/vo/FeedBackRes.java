package com.example.quiz.vo;

import java.util.List;

public class FeedBackRes extends BasicRes {
	
private List<FeedBackVO>feedBackVOList;

public FeedBackRes() {
	super();
	// TODO 自動產生的建構子 Stub
}

public FeedBackRes(int code, String message) {
	super(code, message);
	// TODO 自動產生的建構子 Stub
}

public FeedBackRes(int code, String message,List<FeedBackVO> feedBackVOList) {
	super(code, message);
	this.feedBackVOList = feedBackVOList;
}

public List<FeedBackVO> getFeedBackVOList() {
	return feedBackVOList;
}

public void setFeedBackVOList(List<FeedBackVO> feedBackVOList) {
	this.feedBackVOList = feedBackVOList;
}


}
