package com.jmca.easyauthapplication.controller;


import com.jmca.easyauthapplication.model.User;
import com.jmca.easyauthapplication.payload.request.LoginRequest;
import com.jmca.easyauthapplication.payload.request.SignupRequest;
import com.jmca.easyauthapplication.payload.response.JwtResponse;
import com.jmca.easyauthapplication.payload.response.MessageResponse;
import com.jmca.easyauthapplication.repository.UserRepository;
import com.jmca.easyauthapplication.security.jwt.JwtUtils;
import com.jmca.easyauthapplication.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles =
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        User user =
                new User(
                        signUpRequest.getUsername(),
                        signUpRequest.getEmail(),
                        encoder.encode(signUpRequest.getPassword()));

        if (signUpRequest.getRole() == null || signUpRequest.getRole().equalsIgnoreCase("user")) {
            user.setRole("ROLE_USE");
} else if (signUpRequest.getRole().equalsIgnoreCase("admin")) {
    // Ensure the role is set to 'ROLE_ADMIN' in a case-insensitive manner
    user.setRole(signUpRequest.getRole().toUpperCase() == "ADMIN" ? "ROLE_ADMIN" : "admin");
}
        } else {
            throw new RuntimeException("Role Not Found!");
        }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
