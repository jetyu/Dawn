package com.dawn.constants;

public class Constants {

  public enum UserStatus {
    ACTIVE, // 已激活
    INACTIVE, // 未激活
    SUSPENDED, // 暂停
    BLOCKED // 封禁
  }

  public enum PaperStatus {
    GRADED, // 已批改
    UNGRADED // 未批改
  }

  public enum UserRoles {
    Teacher, Student, Parent, Admin
  }

  public static class PromptText {
    public static final String DEFAULT = "你是一个人工智能助手，按照以下要求回答问题:";
    public static final String COURSE = "课程是";
    public static final String GRADE = "，学习水平是";
    public static final String SIMPLE = "，请直接给出答案。这是问题：";
    public static final String DETAIL = "，要求分析解题过程并且给出详细的答案。按照此模板进行回复。分析过程，解题思路，答案";

    /**
     * OCR单页识别PROMPT
     */
    public static final String OCR_SINGLE_PAGE_PROMPT = """
        请识别图片内容，输出标准JSON，结构如下：
        {
          \"questions\": [
            {
              \"number\": \"题目序号\",
              \"type\": \"单选题|多选题|填空题|解答题\",
              \"content\": \"题目内容\",
              \"options\": \"选项内容，若题型为填空题/解答题则options为空字符串\",
              \"answered\": \"考生是否作答，true=有作答，false=未作答\",
              \"answer\": \"考生答案，如未作答则为空字符串\",
              \"correctAnswer\": \"标准答案\",
              \"titleScore\": \"题目分数\",
              \"studentScore\": \"考生得分，根据考生答案与标准答案计算得出，一致则得满分，否则得0分\"
            }
            // ...更多题目
          ]
        }
        要求：
        - 字段名与上述结构完全一致，questions为数组，所有字段都要有（如无内容填null或空字符串/空数组）。
        - 所有字段必须准确识别后进行输出。
        - answered字段，此字段根据考生在此题目旁边是否作答来判断，如果未发现考生手写答案，则算作未作答，true=有作答，false=未作答。
        - 不要输出任何多余解释或格式说明，只输出JSON。
        - options为数组，若题型为填空题/解答题则options为[]。""";
  }

  public static class ErrorMessage {
    public static final String USER_NOT_ACTIVATED = "用户账户未激活，请联系管理员激活";
    public static final String USER_SUSPENDED = "用户涉嫌违规操作，账户已被禁用！";
    public static final String INVALID_CREDENTIALS = "用户名或密码错误";
    public static final String LOGIN_ERROR = "登录错误，请稍后重试";
    public static final String PHONE_EXISTS = "手机号已被注册，请登录";
    public static final String USERNAME_EXISTS = "用户名已被注册，请登录";
    public static final String USER_NOT_FOUND = "用户名未找到";
    public static final String QUERY_USER_ERROR = "查询用户信息失败，请稍后重试";
    public static final String QUERY_PHONE_ERROR = "查询手机号信息失败，请稍后重试";
    public static final String USERNAME_TAKEN = "用户名已存在";
    public static final String PHONE_TAKEN = "手机号已存在";
    public static final String UPDATE_USER_ERROR = "更新用户信息失败：";
    public static final String DELETE_USER_ERROR = "删除用户失败，请稍后重试";
    public static final String AI_API_KEY_FAILED = "加载AI_API_Key失败，请检查配置文件，Key: {}";
    public static final String AI_API_KEY_REQUIRED = "AI_API_Key未配置，请添加AI_API_Key";
  }
}
