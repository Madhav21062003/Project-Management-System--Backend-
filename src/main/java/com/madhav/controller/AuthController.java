package com.madhav.controller;

import com.madhav.config.JwtProvider;
import com.madhav.entities.Subscription;
import com.madhav.entities.User;
import com.madhav.repository.UserRepository;
import com.madhav.request.LoginRequest;
import com.madhav.response.AuthResponse;
import com.madhav.service.implementation.CustomUserDetailImpl;
import com.madhav.service.interfaces.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CustomUserDetailImpl customUserDetail;

    @PostMapping("/signup")
    public ResponseEntity<Object> createUserHandler(@RequestBody User user) throws Exception {

        User isUserExists = userRepository.findByEmail(user.getEmail());
        if (isUserExists != null){
            throw new Exception("email is already registered with another account");
        }

        User createdUser = new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(createdUser);

        subscriptionService.createSubscription(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setMessage("Signup Success");
        response.setJwt(jwt);

        var list = new ArrayList<Object>();
        list.add(savedUser);
        list.add(response);
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();


        // check username and password is valid or not
        Authentication authentication =  authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Login Successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetail.loadUserByUsername(username);

        if (userDetails == null)
            throw new BadCredentialsException("Incorrect Email");

        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Wrong Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
