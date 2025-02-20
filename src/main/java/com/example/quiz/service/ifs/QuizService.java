package com.example.quiz.service.ifs;

import com.example.quiz.entity.Quiz;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DelReq;
import com.example.quiz.vo.GetQuesRes;
import com.example.quiz.vo.QuizSearchRes;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.UpdateReq;

public interface QuizService {

	public BasicRes create(CreateReq Req); // 包成一包 //create()方法

	public QuizSearchRes getAllQuiz(); // getAllQuiz()方法

	// public SearchRes getAll();

	public QuizSearchRes getQuiz(SearchReq req); // getQuiz

	public GetQuesRes getQuesByQuizId(int quizId); // 取得指定ques_id question table資料

	public BasicRes delete(DelReq req); // delete
	
	public BasicRes update(UpdateReq req); // update

	public QuizSearchRes getQuizById(int id); // 取得指定id quiz table資料
	
	public Quiz updatePublished(int id, boolean published); //改發布
	
	//找指定數量email
	public BasicRes checkFeedbackAmount(int quizId);
}
