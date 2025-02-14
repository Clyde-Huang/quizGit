package com.example.quiz.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@GeneratedValue 可參考投影片SB01的P27
	//該欄位的資料型態是INT時可不加，
	//但用save方法的回傳值中，不會有新的AI值(加了才有)
	//是INTEGER時必加
	//	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	//資料庫來自動增長 出問題就不加上這行
//entity 綁資料庫 表格名稱

@Entity // 將此類別交由 spring boot 託管成 entity(實體) 類(會將類別/屬性跟表/欄位做關聯)
@Table(name = "quiz") // 將此 class Meal 關聯到指定的資料表(quiz)|
public class Quiz {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "start_date")
	private LocalDate startDate;
	@Column(name = "end_date")
	private LocalDate endDate;
	@Column(name = "published")
	private boolean published;

	//
	public Quiz() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public Quiz( String name, String description, LocalDate startDate, LocalDate endDate, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
