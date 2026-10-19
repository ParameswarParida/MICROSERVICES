package com.example.authservice.controller;

import com.example.authservice.entity.User;
import com.example.authservice.model.JwtTokenResponse;
import com.example.authservice.model.LoginRequest;
import com.example.authservice.model.UserDto;
import com.example.authservice.serviceImpl.UserServiceImpl;
import com.example.authservice.util.JWTTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    UserController(UserServiceImpl userService,AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        UserDto userResp = userService.saveUser(user);
        return ResponseEntity.ok(userResp);
    }

    @PostMapping("/genearate-token")
    public ResponseEntity<JwtTokenResponse> genearateToken(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            JwtTokenResponse jwtTokenResponse = userService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(jwtTokenResponse);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
