package com.example.quiz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz.Dao.FeedBackDao;
import com.example.quiz.Dao.QuestionDao;
import com.example.quiz.Dao.QuizDao;
import com.example.quiz.constants.QuesType;
import com.example.quiz.constants.ResMassage;
import com.example.quiz.entity.Feedback;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Qusetion;
import com.example.quiz.service.ifs.FeedbackSercive;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedBackDto;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FeedBackVO;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.OptionAnswer;
import com.example.quiz.vo.OptionCount;
import com.example.quiz.vo.StatisticsDto;
import com.example.quiz.vo.StatisticsRes;
import com.example.quiz.vo.StatisticsVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeedbackSerciveImpl implements FeedbackSercive {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private FeedBackDao feedBackDao;

	@Override
	public BasicRes Fillin(FillinReq req) {
		// 1.檢查參數
		BasicRes checkRes = checkParam(req);
		if (checkRes != null) {
			return checkRes;
		}
		// 2.檢查問卷是否存在&是否是發佈的(可能要喬一下前端)
		if (quizDao.ispublish(req.getQuizId()) != 1) {
			return new BasicRes(ResMassage.QUIZ_NOT_FOUND.getCode(), ResMassage.QUIZ_NOT_FOUND.getMessage());
		}
		// 3.檢查同個email是否有填過同個問卷 (因為這樣設計)
		if (feedBackDao.selectCount(req.getQuizId(), req.getEmail()) != 0) {
			return new BasicRes(ResMassage.EMAIL_DUPLICATED.getCode(), ResMassage.EMAIL_DUPLICATED.getMessage());
		}
		// 4.檢查問題
		// 利用quizId找出問卷(使用JPA)`,被Optional包起來主要是來提醒要判斷內容物是否有值
		Optional<Quiz> op = quizDao.findById(req.getQuizId()); // findById是jpa建好的 所以不用dao自己建
		// 判斷被Optional包起來的quiz物件是否有值
		if (op.isEmpty()) { // op.isEmpty==true時，表示從資料庫取回Quiz沒有資料
			return new BasicRes(ResMassage.QUIZ_NOT_FOUND.getCode(), ResMassage.QUIZ_NOT_FOUND.getMessage());
		}
		// 將Quiz從Optional中取出
		Quiz quiz = op.get();
		// 4.1 檢查填寫問題是否在問卷可填寫的範圍內
		// 所以以下是範圍外 如果範圍外就跳return報錯誤代碼(排除法)
		if (req.getCreateTime().isBefore(quiz.getStartDate()) || req.getCreateTime().isAfter(quiz.getEndDate())) {
			return new BasicRes(ResMassage.CREATEDAY_OUT_OF_DATE_RANGE.getCode(),
					ResMassage.CREATEDAY_OUT_OF_DATE_RANGE.getMessage());
		}
		// 4.2比對相同題號中填寫的答案(來是req)與選項(來自資料庫)是否一樣(除了簡答之外)
		List<Qusetion> quesListtt = questionDao.getQuesByQuizId(req.getQuizId());
		// Map<Integer=題號, List<String=答案
		Map<Integer, List<String>> quesIdAnswerMap = req.getQuesIdAnswerMap();

		for (Qusetion item : quesListtt) {// 遍歷
			// 比對題號
			int quesNumber = item.getQuesId();
			// 排除若該題是必填但沒有回答
			List<String> answerList = quesIdAnswerMap.get(quesNumber);
			if (item.isRequired() && CollectionUtils.isEmpty(answerList)) {
				return new BasicRes(ResMassage.ANSWER_IS_REQUIRE.getCode(), ResMassage.ANSWER_IS_REQUIRE.getMessage());
			}
			// 題目是單選或是簡答(文字)時，答案不能有多個 要看前端怎設計(List 字串 還是其他資料型態)
			if (item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())//
					|| item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				// 答案不能有多個
				if (answerList.size() > 1) {
					return new BasicRes(ResMassage.ONE_OPTION_IS_ALLOWED.getCode(),
							ResMassage.ONE_OPTION_IS_ALLOWED.getMessage());
				}
				// 排除題目類型是 "文字"
				if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
					continue;
				}
			}

			// 將選項字串轉成List 要看前端怎設計(List 字串 還是其他資料型態)!!!改前端比較方便
			// 這邊設計的格式是 陣列 ["aa","bb","cc","dd"]
			/* //List<String>:要確定前端創問卷時 前端的多選選項是陣列 且使用Stringify 轉成字串型態 */

			try {
				List<String> optionssss = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				// 比對相同題號中的選項與回答
				for (String answer : answerList) {
					if (!optionssss.contains(answer)) {
						return new BasicRes(ResMassage.OPTIONS_ANSWER_DISMATCH.getCode(),
								ResMassage.OPTIONS_ANSWER_DISMATCH.getMessage());
					}
				}
			} catch (Exception e) {
				return new BasicRes(ResMassage.OPTIONS_PARSER_ERROR.getCode(),
						ResMassage.OPTIONS_PARSER_ERROR.getMessage());
			}

		}
		// 存資料 用集合存
		List<Feedback> feedbackListt = new ArrayList<>();
		for (Entry<Integer, List<String>> map : req.getQuesIdAnswerMap().entrySet()) {
			Feedback feedback = new Feedback();
			feedback.setQuizId(req.getQuizId());
			feedback.setUserName(req.getUserName());
			feedback.setEmail(req.getEmail());
			feedback.setAge(req.getAge());
			feedback.setQuesId(map.getKey());
			// 將List<String>轉成String

			try {
				String answerStrrr = mapper.writeValueAsString(map.getValue());
				feedback.setAnswer(answerStrrr);
			} catch (JsonProcessingException e) {
				return new BasicRes(ResMassage.OPTIONS_PARSER_ERROR.getCode(),
						ResMassage.OPTIONS_PARSER_ERROR.getMessage());
			}
			feedback.setCreateTime(req.getCreateTime());
			feedbackListt.add(feedback);

		}
		feedBackDao.saveAll(feedbackListt);
		return new BasicRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage());

	}

	private BasicRes checkParam(FillinReq req) {
		// 排除法
		if (req.getQuizId() <= 0) { // 第某題要>=0
			return new BasicRes(ResMassage.PARAM_QWUIZ_ID_ERROR.getCode(),
					ResMassage.PARAM_QWUIZ_ID_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getUserName())) { // 必填Name
			return new BasicRes(ResMassage.PARAM_USER_NAME_ERROR.getCode(),
					ResMassage.PARAM_USER_NAME_ERROR.getMessage());
		}
		if (!StringUtils.hasText(req.getEmail())) { // 必填Email
			return new BasicRes(ResMassage.PARAM_EMAIL_ERROR.getCode(), ResMassage.PARAM_EMAIL_ERROR.getMessage());
		}
		if (req.getAge() <= 0) { // 年齡>=0
			return new BasicRes(ResMassage.PARAM_AGE_ERROR.getCode(), ResMassage.PARAM_AGE_ERROR.getMessage());
		}
		return null;
	}

	@Override
	public FeedBackRes feedback(int quizId) {
		if (quizId <= 0) {
			return new FeedBackRes(ResMassage.PARAM_QWUIZ_ID_ERROR.getCode(),
					ResMassage.PARAM_QWUIZ_ID_ERROR.getMessage());

		}
		List<FeedBackDto> fbList = feedBackDao.selectFeedbackById(quizId);
		// 整理資料
		List<FeedBackVO> feedBackVOList = new ArrayList<>();
		for (FeedBackDto item : fbList) {
			// 檢查是否同一位填寫者(同一個 email)
			// 檢查feedbackvoist中是否有相同email存在
			// 用不到了boolean checkRes = checkEmailExist(feedBackVOList, item.getEmail());
			FeedBackVO resVO = getEmailSame(feedBackVOList, item.getEmail());

			if (resVO != null) { // 表示feedbackvolist 中的 feedbackvo有相同的email(已存在) // 用下面方法判斷

				// 取出optionAnswerList(此optionAnswerList已經有包含前的optionAnswerList)
				List<OptionAnswer> optionAnswerList = resVO.getOptionAnswerList();
				// 設定同一張問卷不同問題以及回答
				OptionAnswer optionAnswer = new OptionAnswer();
				optionAnswer.setQuesId(item.getQuesId());
				optionAnswer.setQuesName(item.getQuesName());
				// 把答案字串轉成list<String>
				List<String> answerList = new ArrayList<>();
				try {
					answerList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
					});

				} catch (Exception e) {
					return new FeedBackRes(ResMassage.ANSWER_PARSER_ERROR.getCode(),
							ResMassage.ANSWER_PARSER_ERROR.getMessage());
				}
				optionAnswer.setAnswerList(answerList);
				optionAnswerList.add(optionAnswer);
				resVO.setOptionAnswerList(optionAnswerList);

				// 取出的feedbackvo早就存在於feedback中 所以不須把resVO加回去
			} else { // 如果不等於(false) 表示feedbackvolist 中的 feedbackvo沒相同的email
				FeedBackVO vo = new FeedBackVO();
				// 設定同一張問卷同一位填寫者的資料
				vo.setQuizId(quizId);
				vo.setQuizName(item.getQuizName());
				vo.setDescription(item.getDescription());
				vo.setUserName(item.getUserName());
				vo.setEmail(item.getEmail());
				vo.setAge(item.getAge());
				vo.setCreateDate(item.getCreateDate());
				// 設定同一張問卷不同問題以及回答
				List<OptionAnswer> optionAnswerList = new ArrayList<>();
				OptionAnswer optionAnswer = new OptionAnswer();
				optionAnswer.setQuesId(item.getQuesId());
				optionAnswer.setQuesName(item.getQuesName());
				// 把答案字串轉成list<String>
				List<String> answerList = new ArrayList<>();
				try {
					answerList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
					});

				} catch (Exception e) {
					return new FeedBackRes(ResMassage.ANSWER_PARSER_ERROR.getCode(),
							ResMassage.ANSWER_PARSER_ERROR.getMessage());
				}
				optionAnswer.setAnswerList(answerList);
				optionAnswerList.add(optionAnswer);
				vo.setOptionAnswerList(optionAnswerList);

				feedBackVOList.add(vo);
			}

		}
		return new FeedBackRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), feedBackVOList);

	}

	private boolean checkEmailExist(List<FeedBackVO> feedBackVOList, String targetEmaill) {// 用不到了
		for (FeedBackVO vo : feedBackVOList) {
			if (vo.getEmail().equalsIgnoreCase(targetEmaill)) {
				return true;
			}
		}
		return false;
	}

	private FeedBackVO getEmailSame(List<FeedBackVO> feedBackVOList, String targetEmaill) {
		for (FeedBackVO vo : feedBackVOList) {
			if (vo.getEmail().equalsIgnoreCase(targetEmaill)) {
				return vo;
			}
		}
		return null;
	}

	@Override
	public StatisticsRes statistics(int quizId) {
		if (quizId <= 0) {
			return new StatisticsRes(ResMassage.PARAM_QWUIZ_ID_ERROR.getCode(), //
					ResMassage.PARAM_QWUIZ_ID_ERROR.getMessage());
		}
		List<StatisticsDto> dtoList = feedBackDao.statistics(quizId);
		// 1. 集合每一題各自的所有答案 : Map<題號，答案>

		// 把每一題的答案放到 quesIdAnswerMap 中
		// 1. 若 quesIdAnswerMap 中已存在相同編號的 quesId //
		// --> 從 quesIdAnswerMap 中取出相同 quesId 對應的答案 List<String>，//
		// 並把轉化後的答案加再一起後並放回到 quesIdAnswerMap 中 //
		// 2. 若 quesIdAnswerMap 中不存在相同編號的 quesId //
		// --> 把轉化後的答案 List<String> 放到 quesIdAnswerMap 中

		// 1. Group dtoList by quesId
		Map<Integer, List<StatisticsDto>> dtoGroupsByQuesId = new HashMap<>();
		for (StatisticsDto dto : dtoList) {
			if (!dtoGroupsByQuesId.containsKey(dto.getQuesId())) {
				dtoGroupsByQuesId.put(dto.getQuesId(), new ArrayList<>());
			}
			dtoGroupsByQuesId.get(dto.getQuesId()).add(dto);
		}

		// 2. Gather answers and options (using the full dtoList as before)
		Map<Integer, List<String>> quesIdAnswerMap = gatherAnswer(dtoList);
		if (quesIdAnswerMap == null) {
			return new StatisticsRes(ResMassage.ANSWER_PARSER_ERROR.getCode(),
					ResMassage.ANSWER_PARSER_ERROR.getMessage());
		}
		List<OptionCount> optionCountList = gatherOptions(dtoList);
		if (optionCountList == null) {
			return new StatisticsRes(ResMassage.OPTIONS_PARSER_ERROR.getCode(),
					ResMassage.OPTIONS_PARSER_ERROR.getMessage());
		}
		optionCountList = computeCount(quesIdAnswerMap, optionCountList);
		if (optionCountList == null) {
			return new StatisticsRes(ResMassage.OPTION_COUNT_ERROR.getCode(),
					ResMassage.OPTION_COUNT_ERROR.getMessage());
		}

		// 3. Set up the result - Iterate through the grouped DTOs to create
		// StatisticsVOs
		List<StatisticsVO> statisticsVOList = new ArrayList<>();
		for (Map.Entry<Integer, List<StatisticsDto>> entry : dtoGroupsByQuesId.entrySet()) {
			Integer quesId = entry.getKey();
			List<StatisticsDto> dtosForQues = entry.getValue();
			StatisticsDto firstDtoForQues = dtosForQues.get(0); // Take the first DTO for question-level info

			StatisticsVO vo = new StatisticsVO();
			vo.setQuizName(firstDtoForQues.getQuizName());
			vo.setQuesId(quesId);
			vo.setQuesName(firstDtoForQues.getQuesName());
			vo.setRequired(firstDtoForQues.isRequired());

			// Get OptionCount list for this question
			List<OptionCount> ocList = new ArrayList<>();
			for (OptionCount oc : optionCountList) {
				if (oc.getQuseId() == quesId) {
					ocList.add(oc);
				}
			}
			vo.setOptionCountList(ocList);
			statisticsVOList.add(vo);
		}
		return new StatisticsRes(ResMassage.SUCCESS.getCode(), ResMassage.SUCCESS.getMessage(), statisticsVOList);
	}

	private Map<Integer, List<String>> gatherAnswer(List<StatisticsDto> dtoList) { // 統計
		Map<Integer, List<String>> quesIdAnswerMap = new HashMap<>();
		for (StatisticsDto item : dtoList) {
			// 如果題型是'文字'就跳過
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				continue; // (跳過 若需要統計就本if註解)
			}
			// 將 Answer String 轉成 List<String>
			List<String> answerList = new ArrayList<>();
			try {
				answerList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
			} catch (Exception e) {
				return null;
			}
			// 若 quesIdAnswerMap 中已經存在相同編號的 List<String> answerList，就從 map 中取出
			if (quesIdAnswerMap.containsKey(item.getQuesId())) {
				List<String> answerListInMap = quesIdAnswerMap.get(item.getQuesId());
				// 把新的答案案已經存在的 answerList 加在一起
				answerList.addAll(answerListInMap);
			}
			quesIdAnswerMap.put(item.getQuesId(), answerList);
		}
		return quesIdAnswerMap;
	}

	private List<OptionCount> gatherOptions(List<StatisticsDto> dtoList) {
		List<OptionCount> optionsCountList = new ArrayList<OptionCount>();
		for (StatisticsDto dto : dtoList) {

			// 跳過題型'文字'因為沒有選項
			if (dto.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				continue;
			}

			// 將轉每輪選項String 為 List<String>
			List<String> optionList = new ArrayList<String>();
			try {
				optionList = mapper.readValue(dto.getOptions(), new TypeReference<>() {
				});
			} catch (Exception e) {
				return null;
			}
			// 蒐集題號和選項
			for (String str : optionList) {
				// 相同題號下每個不同選項會有個OoptionsCount
				OptionCount oc = new OptionCount();
				oc.setQuseId(dto.getQuesId());
				oc.setOption(str);
				optionsCountList.add(oc);
			}

		}
		return optionsCountList;
	}

	// 私有方法 only 多選 單選
	private List<OptionCount> computeCount(Map<Integer, List<String>> quesIdAnswerMap,
			List<OptionCount> optionCountList) {

		Map<Integer, Map<String, OptionCount>> optionCountMapByQuesId = new HashMap<>(); // Map 存储，方便查找

		for (OptionCount oc : optionCountList) {
			int quesId = oc.getQuseId();
			String option = oc.getOption();
			if (!optionCountMapByQuesId.containsKey(quesId)) {
				optionCountMapByQuesId.put(quesId, new HashMap<>());
			}
			optionCountMapByQuesId.get(quesId).put(option, oc); // 题号 -> (选项 -> OptionCount)
		}

		for (Entry<Integer, List<String>> entry : quesIdAnswerMap.entrySet()) {
			int quesId = entry.getKey();
			List<String> answerList = entry.getValue();
			Map<String, OptionCount> optionCountForQues = optionCountMapByQuesId.get(quesId);
			if (optionCountForQues == null)
				continue; // 处理没有选项的题型

			for (String answer : answerList) {
				if (optionCountForQues.containsKey(answer)) {
					optionCountForQues.get(answer).incrementCount(); // OptionCount 中添加一个 count 自增方法
				}
			}
		}

		List<OptionCount> updatedOptionCountList = new ArrayList<>();
		for (Map<String, OptionCount> map : optionCountMapByQuesId.values()) {
			updatedOptionCountList.addAll(map.values());
		}
		return updatedOptionCountList;

	}

	// 2==========
	public FeedBackRes feedback1(int quizId) {
		if (quizId <= 0) {
			return new FeedBackRes(ResMassage.PARAM_QWUIZ_ID_ERROR.getCode(), //
					ResMassage.PARAM_QWUIZ_ID_ERROR.getMessage());
		}
		List<FeedBackDto> feedbackList = feedBackDao.selectFeedbackById(quizId);
		// 整理資料
		List<FeedBackVO> feedbackVoList = new ArrayList<>();
		for (FeedBackDto item : feedbackList) {
			// 把答案字串轉成 List<String>
			List<String> answerList = new ArrayList<>();
			try {
				answerList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
			} catch (Exception e) {
				return new FeedBackRes(ResMassage.ANSWER_PARSER_ERROR.getCode(), //
						ResMassage.ANSWER_PARSER_ERROR.getMessage());
			}
			// 查看 feedbackVoList 中是否已有相同 email 存在
			FeedBackVO resVo = getEmail(feedbackVoList, item.getEmail());
			List<OptionAnswer> optionAnswerList = new ArrayList<>();
			if (resVo != null) {
				// resVo 已存在，不需要創建新的 FeedbackVo
				// 取出 optionAnswerList，此 optionAnswerList 已經有包含之前新增的 optionAnswer
				optionAnswerList = resVo.getOptionAnswerList();
			} else {
				// 取出的 FeedbackVo 早已經存在於 feedbackVoList 中，所以最後不需要再把 resVo 新增回去
				// resVo 不存在，建新的 FeedbackVo
				resVo = new FeedBackVO();
				// 設定同一張問卷和同一位填寫者的資料
				resVo.setQuizId(quizId);
				resVo.setQuizName(item.getQuizName());
				resVo.setDescription(item.getDescription());
				resVo.setUserName(item.getUserName());
				resVo.setEmail(item.getEmail());
				resVo.setAge(item.getAge());
				resVo.setCreateDate(item.getCreateDate());
				// 將 resVo 加入 feedbackVoList 中
				feedbackVoList.add(resVo);
			}
			// 設定同一張問卷不同問題以及答案
			OptionAnswer optionAnswer = new OptionAnswer();
			optionAnswer.setQuesId(item.getQuesId());
			optionAnswer.setQuesName(item.getQuesName());
			optionAnswer.setAnswerList(answerList);
			optionAnswerList.add(optionAnswer);
			resVo.setOptionAnswerList(optionAnswerList);
		}
		return new FeedBackRes(ResMassage.SUCCESS.getCode(), //
				ResMassage.SUCCESS.getMessage(), feedbackVoList);
	}

	private FeedBackVO getEmail(List<FeedBackVO> feedbackVoList, String targetEmail) {
		for (FeedBackVO vo : feedbackVoList) {
			if (vo.getEmail().equalsIgnoreCase(targetEmail)) {
				return vo;
			}
		}
		return null;
	}

	
		@Override
		public Map<String, Object> getFeedbackByQuizId(int quizId) {
		    List<Feedback> feedbackList = feedBackDao.findByQuizId(quizId);
		    
		    if (feedbackList.isEmpty()) {
		        return new HashMap<>(); // 返回空Map而不是null，避免NPE
		    }

		    // 使用 email 分組
		    Map<String, List<Feedback>> groupedByEmail = feedbackList.stream()
		            .collect(Collectors.groupingBy(Feedback::getEmail));
		    
		    // 結果Map
		    Map<String, Object> result = new HashMap<>();
		    result.put("quiz_id", quizId);
		    
		    // 整理每個email的回答
		    List<Map<String, Object>> emailGroups = new ArrayList<>();
		    
		    for (Map.Entry<String, List<Feedback>> entry : groupedByEmail.entrySet()) {
		        List<Feedback> emailFeedbacks = entry.getValue();
		        Map<String, Object> emailGroup = new HashMap<>();
		        
		        // 取得第一筆資料來獲取基本資訊
		        Feedback firstFeedback = emailFeedbacks.get(0);
		        
		        // 整理問題答案對應Map
		        Map<Integer, String> quesIdAnswerMap = new HashMap<>();
		        for (Feedback feedback : emailFeedbacks) {
		            quesIdAnswerMap.put(feedback.getQuesId(), feedback.getAnswer());
		        }
		        
		        // 設置該email組的所有資訊
		        emailGroup.put("quiz_id", firstFeedback.getQuizId());
		        emailGroup.put("user_name", firstFeedback.getUserName());
		        emailGroup.put("email", firstFeedback.getEmail());
		        emailGroup.put("age", firstFeedback.getAge());
		        emailGroup.put("create_time", firstFeedback.getCreateTime()); // 確保每組都有create_time
		        emailGroup.put("ques_id_answer_map", quesIdAnswerMap);
		        
		        emailGroups.add(emailGroup);
		    }
		    
		    result.put("responses", emailGroups);
		    return result;
		}

}