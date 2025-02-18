package com.example.quiz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.ifs.FeedbackSercive;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DelReq;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.GetQuesRes;
import com.example.quiz.vo.QuizSearchRes;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.StatisticsRes;

import jakarta.validation.Valid;

//有用@Valid條件判斷 要加上@Valid 才會生效

@CrossOrigin
@RestController
public class QuizServiceController {

	@Autowired
	private QuizService quizService;

	@Autowired
	private FeedbackSercive feedbackService;

	@PostMapping(value = "quiz/create") // 增
	public BasicRes create(@Valid @RequestBody CreateReq req) {
		return quizService.create(req);
	}

	@GetMapping(value = "quiz/get_all_quiz") // 取得全部 quiz table資料
	public QuizSearchRes getALLQuiz() {
		return quizService.getAllQuiz();
	}

	@PostMapping(value = "quiz/get_quiz") // 時間範圍查詢 JSON那要輸入{} 若要查時間
	public QuizSearchRes getQuiz(@RequestBody SearchReq req) {
		return quizService.getQuiz(req);
	}

	// =====================================================取得指定ques_id question
	// table資料
	// http://localhost:808?/quiz/get_Ques_ById?quizId=1(1=輸入數字)
	// quizId名稱要和方法中的變數名稱一樣
	@PostMapping(value = "quiz/get_Ques_ById")
	public GetQuesRes getQuesByQuizId(@RequestParam("quizId") int quizId) {
		return quizService.getQuesByQuizId(quizId);
	}

	// 呼叫API的路徑: http://localhost:8080/quiz/get_ques_list?quiz_id=1(1=輸入數字)
	// @RequestParam 中的 value，用來指定並對應路經?後面的名稱，並將路徑等號後面的值塞到方法的變數名稱中
	// 以下方法與上面同，只是透過 @RequestParam 中的 value 來指定變數名稱
	@PostMapping(value = "quiz/get_ques_list") //(用這個)
	public GetQuesRes getQuesListByQuizId(@RequestParam(value = "quiz_id") int quizId) {
		return quizService.getQuesByQuizId(quizId);
	}

	// =============================================================================
	// =====================================================取得指定id quiz table資料
	// 取得指定 id (PK) 的 quiz 資料
	@PostMapping("quiz/get_Quiz_ById")
	public QuizSearchRes getQuizById(@RequestParam(value = "id") int id) {
		return quizService.getQuizById(id);
	}
	// =====================================================取得指定ques_id feedback table資料
	// 取得指定 ques_id (PK) 的 feedback 資料 
		//2/13  http://localhost:8080/quiz/get_FB_ById?quiz_id=1
		@PostMapping(value = "/quiz/get_FB_ById") //2/13:用Post會跨域錯誤?
	    public Map<String, Object> getFeedbackByQuizId(@RequestParam(value = "quiz_id") int quizId) {
	        return feedbackService.getFeedbackByQuizId(quizId);
	    }
	
	
	
	
	
	// ===================================================
	// 多個參數使用 @RequestParam
	// 呼叫API的路徑: http://localhost:8080/quiz/search?name=AAA&start_date=2024-12-01
	/*
	 * @GetMapping(value = "quiz/search") public QuizSearchRes search( //
	 * 
	 * @RequestParam(value = "name", required = false, defaultValue = "") String
	 * name,
	 * 
	 * @RequestParam(value = "start_date", required =
	 * false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
	 * 
	 * @RequestParam(value = "end_date", required =
	 * false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
	 * return null; }
	 */
	// ===================================================
	@PostMapping(value = "quiz/update") // 更(全更)
	public BasicRes update(@RequestBody DelReq req) {
		return quizService.delete(req);
	}

	@DeleteMapping(value = "quiz/delete") // 刪
	public BasicRes delete(@RequestBody DelReq req) {
		// 呼叫服務層來處理刪除邏輯
		return quizService.delete(req);
	}

	@PostMapping(value = "quiz/fillin") // 增 使用者填寫
	public BasicRes Fillin(@RequestBody FillinReq req) {
		return feedbackService.Fillin(req);

	}

	// http://localhost:8080/quiz/statistics?quiz_id=1
	@PostMapping(value = "quiz/statistics")
	public StatisticsRes statistics(@RequestParam(value = "quiz_id") int quizId) { // 撈圖表
		return feedbackService.statistics(quizId);
	}

	@PutMapping("quiz/{id}/published") //改不靈
	public Quiz updatePublished(@PathVariable(value = "id") int id,
			@RequestParam(value = "published") boolean published) {

		// 呼叫 service 層來更新 published 屬性
		return quizService.updatePublished(id, published);
	}

}
