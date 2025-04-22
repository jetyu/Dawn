package com.dawn.modules.chat.dto;

public class ChatRequest {
    private String userInputContent;
    private String grade;
    private String subject;
    private String mode;

    public String getUserInputContent() {
        return userInputContent;
    }
    public void setUserInputContent(String userInputContent) {
        this.userInputContent = userInputContent;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
}
