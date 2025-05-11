CREATE TABLE IF NOT EXISTS ocr_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    -- 试卷编号
    paper_id VARCHAR(64),
    -- 题号
    title_id VARCHAR(64),
    -- 题目类型（单选题、多选题、填空题、解答题）
    title_type VARCHAR(64),
    -- 题目内容
    title_content TEXT,
    -- 选项（如是填空题或解答题，则此项为空）
    title_options TEXT,
    -- 考生答案
    student_answer TEXT,
    -- 正确答案
    correct_answer TEXT,
    -- 题目分值
    title_score INT,
    -- 考生分值
    student_score INT,
    -- 是否作答
    answered TINYINT(1) DEFAULT NULL,
    -- 创建时间
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
