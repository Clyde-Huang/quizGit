package com.example.quiz.constants;

public enum ResMassage {
	// 列舉項目 這是用來取代 大量重複出現的常數
	SUCCESS(200, "Success!!"), //
	// Quiz 的屬性
	PARAM_QUIZ_NAME_ERROR(400, "param name error"), //
	PARAM_DESCRIPTION_ERROR(400, "param description error"), //
	PARAM_STARTDATE_ERROR(400, "param startdate error"), //
	PARAM_ENDDATE_ERROR(400, "param enddate error"), //
	// 以下集合(Question)
	PARAM_QUESID_ERROR(400, "param quesid error"), //
	PARAM_QUESNAME_ERROR(400, "param question name error"), //
	PARAM_TYPE_ERROR(400, "param typr error"), //
	PARAM_OPTIONS_ERROR(400, "param options error"),//
	PARAM_QUESLIST_ERROR(400, "param question list error"), //長度不為0
	// 常數都大寫，用_連接單字
	DATA_SAVE_ERROR(400,"Data save error"),
	DATA_UPDATE_ERROR(400,"Data update error"),
	QUES_TYPE_MISMATCH(400,"type mismatch"),
	PARAM_QWUIZ_ID_ERROR(400,"param q i e"),
	QUIZ_NOT_FOUND(404,"quiz not found" ),
	QUIZ_ID_MISSMATCH(400,"quiz id mismatch"),
	PARAM_USER_NAME_ERROR(400,"param user name error"),
	PARAM_EMAIL_ERROR(400,"param email error"),
	PARAM_AGE_ERROR(400,"param age error"),
	EMAIL_DUPLICATED(400,"email duplicate"),
	CREATEDAY_OUT_OF_DATE_RANGE(400,"createday out of date range"),
	ANSWER_IS_REQUIRE(400,"answer is require"),
	ONE_OPTION_IS_ALLOWED(400,"one option is allowed"),
	OPTIONS_PARSER_ERROR(400,"options parser error"),
	OPTIONS_ANSWER_DISMATCH(400,"options answer dismatched"),
	ANSWER_PARSER_ERROR(400,"answer parser error"),
	OPTION_COUNT_ERROR(400,"option count error");
	

	private int Code;
	private String message;

	private ResMassage(int statusCode, String message) {

		this.Code = statusCode;
		this.message = message;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
