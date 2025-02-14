package com.example.quiz.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.entity.Qusetion;
import com.example.quiz.entity.QusetionId;
@Repository
public interface QuestionDao extends JpaRepository<Qusetion, QusetionId> {

	@Query(value="select * from question where quiz_id = ?" ,nativeQuery=true) //透過Id查
	public List<Qusetion> getQuesByQuizId(int quizId);
	
	@Transactional
	@Modifying
	@Query(value="delete  from question where quiz_id in (?) ",nativeQuery=true) //刪
	public int delByQuizIdIn(List<Integer> quizIds);
}
