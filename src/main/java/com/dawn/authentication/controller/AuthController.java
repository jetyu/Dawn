package com.dawn.authentication.controller;

import com.dawn.authentication.utils.JwtUtils;
import com.dawn.constants.Constants;
import com.dawn.modules.user.model.User;
import com.dawn.modules.user.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            // Spring Security 认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            // 获取用户详情
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userToken = jwtUtils.generateToken(userDetails);
            // 获取用户信息并更新登录时间
            User user = userService.updateLoginUser(username, password);

            Map<String, Object> response = new HashMap<>();
            response.put("token", userToken);
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("name", user.getNickname());
            response.put("role", user.getRole());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", Constants.ErrorMessage.LOGIN_ERROR);
            errorResponse.put("message", Constants.ErrorMessage.INVALID_CREDENTIALS);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(Map.of(
                "userId", registeredUser.getId(),
                "username", registeredUser.getUsername()));
    }
}