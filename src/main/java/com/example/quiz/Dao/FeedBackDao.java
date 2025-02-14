package com.example.quiz.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Feedback;
import com.example.quiz.entity.FeedbackId;
import com.example.quiz.vo.FeedBackDto;
import com.example.quiz.vo.StatisticsDto;

@Repository
public interface FeedBackDao extends JpaRepository<Feedback, FeedbackId> {

	// 檢查同個email是否有填過同個問卷
	@Query(value = "select count(quiz_id) from feedback where quiz_id=?1 and email=?2 ", nativeQuery = true) //
	public int selectCount(int quizId, String email);

	/**
	 * 1. 因為使用 join 撈取多張表的資料，撈回來的資料用 FeedbackDto 裝載</br>
	 * 2. FeedbackDto 不是資料表，所以 nativeQuery = false</br>
	 * 3. nativeQuery = false 時，語法中原本表的名稱會變成 Entity class 名稱，欄位的名稱會變成屬性變數名稱</br>
	 * 4. FeedbackDto 不像 Entity 有被 Spring boot 託管(class 上有加上 @Entity 的註釋)，所以必須要透過
	 * new FeedbackDto() 的方式來建構(建構方法)，且前面要加上完整路徑(com.example.quiz12.vo)</br>
	 * 5. 語法中的建構方法寫法在 FeedbackDto 中也要有對應的建構方法
	 */

	@Query(value = "select new com.example.quiz.vo.FeedBackDto(Qz.name,Qz.description,f.userName,"
			+ " f.email,f.age,f.answer,f.createTime,Qu.quesId,Qu.questName) " //
			+ " from Quiz Qz" //
			+ " join Qusetion Qu"//
			+ " on Qz.id= Qu.quizId" //
			+ " join Feedback as f" //
			+ " on Qz.id=f.quizId "//
			+ " and Qu.quesId=f.quesId"//
			+ " where Qz.id=?1" //
			,  nativeQuery = false) // Quiz Qusetion Feedback 都是entity類名 因為nativeQuery (= false)
									
	public List<FeedBackDto> selectFeedbackById(int quizId);
	
	
		//1.可能要用別名 2.資料型態要對整齊(Dto 順序是 name,age 這邊就要XX.name,OO.age 不能相反 應為它自動對應)
	@Query(value = "select new com.example.quiz.vo.StatisticsDto(" //
			+ " Qz.name,Qu.quesId,Qu.questName,Qu.required, "
			+ " Qu.options,Qu.type,f.answer)" //
			+ " from Quiz Qz"//
			+ " join Qusetion Qu"//
			+ " on Qz.id= Qu.quizId" //
			+ " join Feedback as f" //
			+ " on Qz.id=f.quizId "//
			+ " and Qu.quesId=f.quesId"//
			+ " where Qz.id=?1" //
			,  nativeQuery = false)
	public List<StatisticsDto> statistics(int quizId);
	
	
	//2/13
	List<Feedback> findByQuizId(int quizId);
	
	
}
