package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.quiz.Dao.QuestionDao;
import com.example.quiz.Dao.QuizDao;
import com.example.quiz.constants.QuesType;
import com.example.quiz.constants.ResMassage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Qusetion;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DelReq;
import com.example.quiz.vo.GetQuesRes;
import com.example.quiz.vo.QuizSearchRes;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.UpdateReq;

import jakarta.transaction.Transactional;

@Service
public class QuizServiceimpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Transactional(rollbackOn = Exception.class)
	// rollbackOn =發生例外Exception時@Transactional
	// @Transactional: 因為同時新增問卷和問題，新增多筆資料都算是同一次的行為，所以要嘛全部成功，要嘛全部失敗
	// rollbackOn = Exception.class: 指定@Transactional 資料回朔有效的例外層級
	// 發生例外(Exception)是 RuntimeException 或其子類別時，@Transactional 才會讓資料回朔，
	// 藉由 rollbackOn 可以指定發生哪個例外時，就可以讓資料回朔

	@Override
	public BasicRes create(CreateReq req) {
		// 檢查參數
		BasicRes checkRes = checkParam(req);
		if (checkRes != null) {
			return checkRes;
		}
		// 因為新增問卷 所以問卷的id一定是0`, 但 req 無
		//
		// 因為 quiz 的 PK 是流水號，不會重複寫入，所以不用檢查資料料庫是否已存在相同的 PK
		// 新增問卷
		// 因為 Quiz 中的 id 是 AI 自動生成的流水號，要讓 quizDao 執行 save 後可以把該 id 的值回傳，
		// 必須要在 Quiz 此 Entity 中將資料型態為 int 的屬性 id
		// 加上 @GeneratedValue(strategy = GenerationType.IDENTITY) /在bean那邊加 entity
		// JPA 的 save，PK 已存在於 DB，會執行 update，若PK不存在，則會執行 insert ***********

		try {
			Quiz quiz = quizDao.save(new Quiz(req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(),
					req.isPublished()));
			// 把 quiz 塞到 question 中的quizId
			for (Qusetion question : req.getQuestionList()) {
				question.setQuizId(quiz.getId());// setQuizId??
			}
			// 新增問題 : 把question list 寫 進 DB(database)
			questionDao.saveAll(req.getQuestionList());
			return new BasicRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage());

		} catch (Exception e) {
			return new BasicRes(ResMassage.DATA_SAVE_ERROR.getCode(), ResMassage.DATA_SAVE_ERROR.getMessage());
		}

	}

	private BasicRes checkParam(CreateReq req) {
		// 排除法一一檢查
		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMassage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMassage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMassage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMassage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
		if (req.getStartDate() == null) {
			return new BasicRes(ResMassage.PARAM_STARTDATE_ERROR.getCode(),
					ResMassage.PARAM_STARTDATE_ERROR.getMessage());
		}
		if (req.getEndDate() == null) {
			return new BasicRes(ResMassage.PARAM_ENDDATE_ERROR.getCode(), ResMassage.PARAM_ENDDATE_ERROR.getMessage());
		}
		// 檢查開始時間不能比結束時間晚
		// 用isAfter或before判斷
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMassage.PARAM_ENDDATE_ERROR.getCode(), "開始時間不能比結束時間晚");
		}
		// =====檢查問題內容
		List<Qusetion> quesList = req.getQuestionList();
		if (req.getQuestionList().size() <= 0) {
			return new BasicRes(ResMassage.PARAM_QUESLIST_ERROR.getCode(),
					ResMassage.PARAM_QUESLIST_ERROR.getMessage());
		}
		for (Qusetion item : quesList) {
			// 問題編號從1開始，但無法檢查是否有按照順序
			if (item.getQuesId() <= 0) {
				return new BasicRes(ResMassage.PARAM_QUESID_ERROR.getCode(),
						ResMassage.PARAM_QUESID_ERROR.getMessage());
			}
			if (!StringUtils.hasText(item.getQuestName())) {
				return new BasicRes(ResMassage.PARAM_QUIZ_NAME_ERROR.getCode(),
						ResMassage.PARAM_QUIZ_NAME_ERROR.getMessage());
			}
			if (!StringUtils.hasText(item.getType())) {
				return new BasicRes(ResMassage.PARAM_TYPE_ERROR.getCode(), ResMassage.PARAM_TYPE_ERROR.getMessage());
			}
			/*
			 * if (!StringUtils.hasText(item.getOptions())) { return new
			 * BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(),
			 * ResMassage.PARAM_OPTIONS_ERROR.getMessage()); }
			 */
			// 檢查 options 欄位
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType()) && StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(),
						ResMassage.PARAM_OPTIONS_ERROR.getMessage());
			}

			if ((item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())
					|| item.getType().equalsIgnoreCase(QuesType.MULTIPLE.getType()))
					&& !StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(),
						ResMassage.PARAM_OPTIONS_ERROR.getMessage());
			}

			/*
			 * // 檢查1.type是否是單選,多選,文字 if
			 * (!item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())// ||
			 * !item.getType().equalsIgnoreCase(QuesType.MULTIPLE.getType())// ||
			 * !item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) { return new
			 * BasicRes(ResMassage.QUES_TYPE_MISMATCH.getCode(),
			 * ResMassage.QUES_TYPE_MISMATCH.getMessage()); } // 2.文字類型時option不能有值
			 * 3.非文字類型時option要有值 if
			 * (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())// &&
			 * StringUtils.hasText(item.getOptions())) { return new
			 * BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(),
			 * ResMassage.PARAM_OPTIONS_ERROR.getMessage()); } }
			 */
			// 檢查問題類型 (單選、多選、文字)
			String type = item.getType();
			if (!type.equals("單選") && !type.equals("多選") && !type.equals("文字")) {
				return new BasicRes(ResMassage.QUES_TYPE_MISMATCH.getCode(), "問題類型必須是單選、多選或文字");
			}

			// 檢查選項：文字類型不應該有選項，其他類型必須有選項
			if ("文字".equals(type) && StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(), "文字類型問題不應該有選項");
			}

			if (!"文字".equals(type) && !StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMassage.PARAM_OPTIONS_ERROR.getCode(), "非文字類型問題必須提供選項");
			}
		}
		return null;

	}

	@Override
	public QuizSearchRes getAllQuiz() {
		// TODO 自動產生的方法 Stub
		List<Quiz> res = quizDao.getAllQuiz();
		return new QuizSearchRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), res);
	}

	@Override
	public QuizSearchRes getQuiz(SearchReq req) {
		// 如果name 沒有條件 前端帶過資料可能會是null或空字串
		// 改變條件值 如果name是null或是空字串或是全空粽串 一律改為空字串
		String name = req.getName();
		if (!StringUtils.hasText(name)) { // SQL語法中 like%%(兩個%中間是空字串)表示，忽略該欄位的條件值
			name = "";
		}
		LocalDate startDate = req.getStartDate();
		if (startDate == null) { // 表示前時間沒有帶值(起始時間無值)
			startDate = LocalDate.of(1970, 1, 1);
		}
		LocalDate endDate = req.getEndDate();
		if (endDate == null) { // 表示後時間沒有帶值(結束時間無值)
			endDate = LocalDate.of(2999, 12, 1);
		}
		List<Quiz> res = quizDao.getQuiz(name, startDate, endDate);
		return new QuizSearchRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), res);
	}

	@Override
	public GetQuesRes getQuesByQuizId(int quizId) {
		if (quizId <= 0) {
			return new GetQuesRes(ResMassage.PARAM_QWUIZ_ID_ERROR.getCode(),
					ResMassage.PARAM_QWUIZ_ID_ERROR.getMessage());
		}
		List<Qusetion> res = questionDao.getQuesByQuizId(quizId);
		return new GetQuesRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), res);

	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes delete(DelReq req) {
		try {
			quizDao.delByQuizIdIn(req.getQuizIdList());
			questionDao.delByQuizIdIn(req.getQuizIdList());
		} catch (Exception e) {
			return new BasicRes(ResMassage.DATA_SAVE_ERROR.getCode(), ResMassage.DATA_SAVE_ERROR.getMessage());
		}
		return new BasicRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage());
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) {
		// 檢查參數
		BasicRes checkRes = checkParam(req);
		if (checkRes != null) {
			return checkRes;
		}
		// 檢查 1.quizId>0 2.檢查 quizId是否有資料存在
		int count = quizDao.selectCount(req.getQuizId());
		if (count != 1) {
			return new BasicRes(ResMassage.QUIZ_NOT_FOUND.getCode(), ResMassage.QUIZ_NOT_FOUND.getMessage());
		}

		// 檢查問題中的quizId是否重複()
		for (Qusetion item : req.getQuestionList()) {
			if (req.getQuizId() != item.getQuizId()) {
				return new BasicRes(ResMassage.QUIZ_ID_MISSMATCH.getCode(), ResMassage.QUIZ_ID_MISSMATCH.getMessage());
			}
		}

		try {
			// 1.更新quiz by quizId
			quizDao.updateById(req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(),
					req.isPublished(), req.getQuizId());

			// 2.刪除 questions by quizId
			questionDao.delByQuizIdIn(List.of(req.getQuizId()));

			// 3.新增 questions
			questionDao.saveAll(req.getQuestionList());

		} catch (Exception e) {
			return new BasicRes(ResMassage.DATA_UPDATE_ERROR.getCode(), ResMassage.DATA_UPDATE_ERROR.getMessage());
		}
		return new BasicRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage());
	}

	@Override
	public QuizSearchRes getQuizById(int id) {
		// 檢查 id 是否有效
		if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "請提供有效的 ID");
        }

        Optional<Quiz> quiz = quizDao.findById(id); // 使用 Optional 來處理可能的 null

        if (!quiz.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到對應的 Quiz");
        }

        List<Quiz> quizList = Collections.singletonList(quiz.get());

        return new QuizSearchRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), quizList);
    
	}
	
	 // 只更新 published 屬性
	@Override
    public Quiz updatePublished(int id, boolean published) {
        // 查詢指定 ID 的 Quiz 資料
        Quiz quiz = quizDao.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));

        // 更新 published 屬性
        quiz.setPublished(published);

        // 使用 save() 來儲存更新後的 Quiz
        return quizDao.save(quiz);
    }
	
	
	
}
