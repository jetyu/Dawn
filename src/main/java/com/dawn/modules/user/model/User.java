package com.dawn.modules.user.model;

import com.dawn.constants.Constants.UserRoles;
import com.dawn.constants.Constants.UserStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 18, unique = true)
    private String phone;
    @Column(nullable = false, length = 20, unique = true)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = true)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRoles role = UserRoles.Student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date userCreateTime = new java.util.Date();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date userLastLoginTime = new java.util.Date();
}