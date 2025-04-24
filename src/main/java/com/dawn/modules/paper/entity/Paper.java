package com.dawn.modules.paper.entity;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "papers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Paper {
    @Id
    private String id;
    private String name;
    private String subject;
    private String path;
    private LocalDateTime uploadTime;
    private String status; // 批改状态
}
