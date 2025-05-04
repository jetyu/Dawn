package com.dawn.modules.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.dawn.modules.user.model.User;
import com.dawn.modules.user.repository.UserRepository;
import com.dawn.constants.Constants;
import com.dawn.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getAllUsers(Pageable pageable, String query) {
        log.info("查询条件{}", query);
        if (query == null || query.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByIdOrNameOrPhone(query, pageable);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#username")
    public Optional<User> findByUsername(String username) {
        log.debug("根据用户名查询用户：{}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByPhone(String phone) {
        log.debug("根据手机号查询用户：{}", phone);
        return userRepository.findByPhone(phone);
    }

    private void validateUserStatus(User user) {
        switch (user.getStatus()) {
            case Constants.UserStatus.INACTIVE:
                throw new BusinessException(Constants.ErrorMessage.USER_NOT_ACTIVATED);
            case Constants.UserStatus.SUSPENDED:
                throw new BusinessException(Constants.ErrorMessage.USER_SUSPENDED);
            default:
                break;
        }
    }

    private User updateLoginUserInfo(User user) {
        user.setUserLastLoginTime(new java.util.Date());
        return userRepository.save(user);
    }

    public User updateLoginUser(String username, String password) {
        Optional<User>  optUserOptional = userRepository.findByUsername(username);
        User user = optUserOptional.orElseThrow(() -> new BusinessException(Constants.ErrorMessage.USER_NOT_FOUND));
        this.validateUserStatus(user);
        this.updateLoginUserInfo(user);
        return user;
    }

    public User register(User user) {
        validateNewUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void validateNewUser(User user) {
        log.debug("验证新用户信息：{}", user.getUsername());
        Optional<User> existingUser = findByUsername(user.getUsername());
        Optional<User> existingPhone = findByPhone(user.getPhone());
        if (existingUser.isPresent()) {
            throw new BusinessException(Constants.ErrorMessage.USERNAME_EXISTS);
        }
        if (existingPhone.isPresent()) {
            throw new BusinessException(Constants.ErrorMessage.PHONE_EXISTS);
        }
    }

    @Cacheable(value = "users", key = "#id")
    public Optional<User> findById(Long id) {
        log.debug("根据ID查询用户：{}", id);
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        log.info("更新用户信息：{}", user.getUsername());
        validateUpdateUser(user);
        handlePasswordUpdate(user);
        return userRepository.save(user);
    }

    private void validateUpdateUser(User user) {
        log.debug("验证更新用户信息：{}", user.getUsername());
        Optional<User> existingUsername = findByUsername(user.getUsername());
        if (existingUsername.isPresent() && !existingUsername.get().getId().equals(user.getId())) {
            throw new BusinessException(Constants.ErrorMessage.USERNAME_TAKEN);
        }

        Optional<User> existingPhone = findByPhone(user.getPhone());
        if (existingPhone.isPresent() && !existingPhone.get().getId().equals(user.getId())) {
            throw new BusinessException(Constants.ErrorMessage.PHONE_TAKEN);
        }
    }

    private void handlePasswordUpdate(User user) {
        log.debug("处理用户密码更新：{}", user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            Optional<User> currentUser = findById(user.getId());
            currentUser.ifPresent(u -> user.setPassword(u.getPassword()));
        }
    }

    public void deleteUser(Long id) {
        log.info("删除用户：{}", id);
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("删除用户失败：{}", e.getMessage());
            throw new BusinessException(Constants.ErrorMessage.DELETE_USER_ERROR);
        }
    }
}