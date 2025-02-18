package com.example.quiz.entity;
//entity 綁資料庫 表格名稱

import com.example.quiz.constants.ResMassage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

//雙PK
//@IdClass 一個Entity 中若有多個PK ，就必須要再升成一個類 來管理雙PK 此@用來關聯class
@IdClass(value = QusetionId.class) // 將 New_MealId 類作為�� PK 的���值對��類別
//複合主鍵: 有兩個或以上的屬性有加上 @Id
// 複合主鍵的唯一性就會是這些""全部""有加上 @Id 的屬性的組合出來的值不重複
// 複合主鍵中單一個屬性可以重複

@Entity // 將此類別交由 spring boot 託管成 entity(實體) 類(會將類別/屬性跟表/欄位做關聯)
@Table(name = "question") // 將此 class Meal 關聯到指定的資料表
public class Qusetion {

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	//@Min表示至'少'value(有包含，就是至少1題)
	@Min(value = 1,message=ResMassage.Constants.PARAM_QUES_ID_ERROR_MSG)
	@Id
	@Column(name = "ques_id")
	private int quesId;

	
	@NotBlank(message = ResMassage.Constants.PARAM_QUESNAME_ERROR_MSG )
	@Column(name = "quest_name")
	private String questName;

	@NotBlank(message = ResMassage.Constants.PARAM_TYPE_ERROR_MSG )
	@Column(name = "type")
	private String type;

	@Column(name = "required")
	private boolean required;

	@Column(name = "options")
	private String options;

	//
	public Qusetion() {
		super();

	}

	public Qusetion(int quesId, int quizId, String questName, String type, boolean required, String options) {
		super();
		this.quesId = quesId;
		this.quizId = quizId;
		this.questName = questName;
		this.type = type;
		this.required = required;
		this.options = options;
	}

	public int getQuesId() {
		return quesId;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getQuestName() {
		return questName;
	}

	public String getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	public String getOptions() {
		return options;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}
	//2/5 3:26 錯誤原因加成setQuesId
	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

}
