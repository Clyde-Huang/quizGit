package com.example.quiz.service.ifs;

import java.util.Map;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.GetQuesRes;
import com.example.quiz.vo.StatisticsRes;

public interface FeedbackSercive {

	public BasicRes Fillin(FillinReq req); //生成資料進feedback
	
	public FeedBackRes feedback(int quizId);
	
	public StatisticsRes statistics (int quizId);
	
	
	//2/13
	public Map<String, Object> getFeedbackByQuizId(int quizId);
	
	
	
}
