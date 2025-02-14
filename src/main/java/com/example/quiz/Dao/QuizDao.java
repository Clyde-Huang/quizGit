package com.example.quiz.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.entity.Quiz;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

	/**
	 * 1. 因為透過 join 來取得跨表的欄位，所以 nativeQuery 只能是 false <br>
	 * 2. nativeQuery = false，語法中表的名稱會改成 Entity class 的名稱，欄位名稱則是 Entity 的屬性變數名稱<br>
	 * 3. nativeQuery = false 時，用來裝載的類別 SearchVo 必須要透過 new<br>
	 * 4. 因為 SearchVo 沒有被 Spring boot 託管，所以要跟加上路徑
	 * 
	 * @Query(value = "select new com.example.quiz.vo.SearchVO(quizId, name,
	 *              description, startDate, endDate, published, questionList ) " //
	 *              + " from Quiz join Qusetion on Quiz.id=Qusetion.quiz_id",
	 *              nativeQuery = false) // 這邊不用原生語法 所以false public List<SearchVO>
	 *              getALL();
	 */
	@Query(value = "select * from quiz", nativeQuery = true)
	public List<Quiz> getAllQuiz();

	@Query(value = "select * from quiz where name like %?1% and start_date >=?2 and end_date<=?3", nativeQuery = true)
	public List<Quiz> getQuiz(String name, LocalDate sD, LocalDate eD);

	@Transactional
	@Modifying
	@Query(value = "delete  from quiz where id in (?) ", nativeQuery = true)
	public int delByQuizIdIn(List<Integer> quizIds);

	
	
	@Query(value = "select count(id) from quiz where id=? ", nativeQuery = true) //查Id 是否有資料 1=有 0=無 所以是資料型態int
	public int selectCount(int quizId);

	@Transactional
	@Modifying
	@Query(value = " update quiz set name =?1,description=?2,start_date=?3,end_date=?4,published=?5 where id=?6", nativeQuery = true)
	public int updateById(String name, String description, LocalDate startDate, LocalDate endDate, boolean published,int id);
	
	@Query(value = "select count(id) from quiz where id=? and published=1 ", nativeQuery = true) //查Id &是否publish=true=1
	public int   ispublish(int quizid);
	
	@Query(value = "select * from quiz where id=? ", nativeQuery = true) //不用這個用JPA(findById(int id))
	public Quiz getById(int id);

}
