CREATE TABLE IF NOT EXISTS `quiz` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '流水號',
  `name` varchar(145) DEFAULT NULL,
  `description` varchar(900) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `published` tinyint DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='問卷';
-- 這些資料哪來:SQL要的table右鍵copy to clipboard > create statement
-- 手動加上 IF NOT EXISTS

CREATE TABLE IF NOT EXISTS `question` (
  `quiz_id` int NOT NULL COMMENT '問卷編號 對應 quiz的id欄位',
  `ques_id` int NOT NULL,
  `quest_name` varchar(545) DEFAULT NULL COMMENT '問題名稱',
  `type` varchar(45) DEFAULT NULL COMMENT '單選多選',
  `required` tinyint DEFAULT '0' COMMENT '是否必填',
  `options` varchar(900) DEFAULT NULL,
  PRIMARY KEY (`quiz_id`,`ques_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='第幾個題目quiz的某幾題question';

CREATE TABLE IF NOT EXISTS `feedback` (
  `quiz_id` int NOT NULL,
  `user_name` varchar(145) DEFAULT NULL,
  `email` varchar(145) NOT NULL,
  `age` int DEFAULT NULL,
  `ques_id` int NOT NULL,
  `answer` varchar(2000) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  PRIMARY KEY (`quiz_id`,`email`,`ques_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

