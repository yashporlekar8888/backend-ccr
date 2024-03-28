package com.ccr.mvp.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ccr.mvp.model.Role;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        
      if (userRepository.count() == 0) {

        User admin = User
                      .builder()
                      .userName("Super Admin")
                      .email("admin@gmail.com")
                      .password(passwordEncoder.encode("123"))
                      .phoneNumber((long) 1545789632)
                      .role(Role.ROLE_SUPERADMIN)
                      .build();

        userService.saveUser(admin);
        log.debug("created ADMIN user - {}", admin);
      }
    }

}