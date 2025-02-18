package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz.constants.ResMassage;
import com.example.quiz.entity.Qusetion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReq { // Req只要生成get set即可(?) 建構方法有postman
	//問卷--
	
	@NotBlank(message = ResMassage.Constants.PARAM_QUIZ_NAME_ERROR_MSG) //使用於字元或字串，限制不能為 Null 以及長度至少為一個非空白字元或字串
	//message表示不符合限制的時候要回傳的訊息，等號後面只能放'固定'(常數)的字串
	//PARAM_QUIZ_NAME_ERROR_MSG 要宣告成 final 才會是常數
	//PARAM_QUIZ_NAME_ERROR_MSG 要宣告成 static 才會是'固定'的字串
	private String name;

	
	@NotBlank(message = ResMassage.Constants.PARAM_DESCRIPTION_ERROR_MSG)
	private String description;

	@NotNull(message=ResMassage.Constants.PARAM_STARTDATE_ERROR_MSG)
	private LocalDate startDate;
	
	@NotNull(message=ResMassage.Constants.PARAM_ENDDATE_ERROR_MSG)
	private LocalDate endDate;

	private boolean published;
	//問卷--
	
	
	
	@Valid //因為Qusetion 裡面還有對屬性有加上驗證限制，屬於嵌套驗證，要加上@Valid才會讓Question中的限制生效
	//@Size(min=1 )//至少一題(一組)
	@NotEmpty (message=ResMassage.Constants.PARAM_QUESLIST_ERROR_MSG) //兩個都可 集合不能為空
	private List<Qusetion> questionList;  //題目

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<Qusetion> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Qusetion> questionList) {
		this.questionList = questionList;
	}

	public CreateReq() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public CreateReq(String name, String description, LocalDate startDate, LocalDate endDate, boolean published,
			List<Qusetion> questionList) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
		this.questionList = questionList;
	}

}
