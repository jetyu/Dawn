package com.dawn.modules.chat.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRequest {
    private String userInputContent;
    private String grade;
    private String subject;
    private String mode;
}
