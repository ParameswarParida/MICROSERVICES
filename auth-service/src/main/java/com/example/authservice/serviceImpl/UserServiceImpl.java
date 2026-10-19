package com.example.authservice.serviceImpl;

import com.example.authservice.entity.User;
import com.example.authservice.model.JwtTokenResponse;
import com.example.authservice.model.UserDto;
import com.example.authservice.repo.UserRepo;
import com.example.authservice.util.JWTTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtil jwtTokenUtil;
    UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,JWTTokenUtil jwtTokenUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserDto saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        UserDto userDto = new UserDto();
        userDto.setUsername(savedUser.getUsername());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRoles(savedUser.getRoles());
        userDto.setId(savedUser.getId());
        return userDto;
    }

    public JwtTokenResponse generateToken(String username){
        String token = jwtTokenUtil.generateToken(username);
        JwtTokenResponse response = new JwtTokenResponse();

        response.setToken(token);
        response.setTokenType("Bearer");
        return response;
    }
}
