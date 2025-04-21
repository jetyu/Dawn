package com.dawn.modules.user.controller;

import com.dawn.modules.user.model.User;
import com.dawn.modules.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @Operation(summary = "查询所有用户", description = "支持分页和搜索功能")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @Parameter(description = "页码") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(name = "size", defaultValue = "15") int size,
            @Parameter(description = "搜索关键词") @RequestParam(name = "query", required = false) String query) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsers(pageable, query);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "用户信息") @Valid @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}