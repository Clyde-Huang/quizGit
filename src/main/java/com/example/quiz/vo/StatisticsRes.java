package com.example.quiz.vo;

import java.util.List;

public class StatisticsRes extends BasicRes {

	private List<StatisticsVO> statisticsVOList;

	public StatisticsRes() {
		super();
		// TODO 自動產生的建構子 Stub
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
		// TODO 自動產生的建構子 Stub
	}

	public StatisticsRes(int code, String message,List<StatisticsVO> statisticsVOList) {
		super(code, message);//上面值貼過來
		this.statisticsVOList = statisticsVOList;
	}

	public List<StatisticsVO> getStatisticsVOList() {
		return statisticsVOList;
	}

	public void setStatisticsVOList(List<StatisticsVO> statisticsVOList) {
		this.statisticsVOList = statisticsVOList;
	}

}
