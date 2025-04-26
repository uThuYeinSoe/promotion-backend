package com.promotion.auth;

import com.promotion.config.JWTService;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import com.promotion.utility.RandomId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private Role role;

    public RegisterResponse register(RegisterRequest request) {

        String randomId = RandomId.generateRandomId();

        var user = User.builder()
                .randomId(randomId)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .parentId(request.getParentId())
                .build();
        repository.save(user);

        return RegisterResponse.builder()
                .statusMessage("Register Successfully")
                .status(true)
                .statusCode(201)
                .randomId(user.getRandomId())
                .build();

    }

    public RegisterResponse agentRegister(String adminRandomId,RegisterRequest request) {

        String randomId = RandomId.generateRandomId();

        var user = User.builder()
                .randomId(randomId)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.AGENT)
                .parentId(adminRandomId)
                .build();

        repository.save(user);

        return RegisterResponse.builder()
                .statusMessage("Register Successfully")
                .status(true)
                .statusCode(201)
                .randomId(user.getRandomId())
                .build();

    }


    public RegisterResponse userRegister(RegisterRequest request) {

        Optional<User> user = repository.findByRandomId(request.getParentId());
        if(user.isEmpty()){
            return RegisterResponse.builder()
                    .statusMessage("Register Failed")
                    .status(false)
                    .statusCode(401)
                    .randomId("Your Agent Id something wrong")
                    .build();
        }

        User agentUser = user.get();

        String randomId = RandomId.generateRandomId();

        var userObj = User.builder()
                .randomId(randomId)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .parentId(agentUser.getRandomId())
                .build();

        repository.save(userObj);

        return RegisterResponse.builder()
                .statusMessage("Register Successfully")
                .status(true)
                .statusCode(201)
                .randomId(userObj.getRandomId())
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getRandomId(),
                            request.getPassword()
                    )
            );

            // Fetch user details after successful authentication
            var user = repository.findByRandomId(request.getRandomId())
                    .orElseThrow(() -> new Exception("User not found"));

            // Generate JWT token
            var jwtToken = jwtService.generateToken(user);

            // Return success response
            return AuthenticationResponse
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("Login Successfully")
                    .token(jwtToken)
                    .randomId(user.getRandomId())
                    .build();

        } catch (BadCredentialsException e) {
            // Catch invalid credentials and return Unauthorized status
            System.out.println(e);
            return AuthenticationResponse
                    .builder()
                    .status(false)
                    .statusCode(401)  // Unauthorized
                    .statusMessage("Invalid credentials")
                    .token(null)
                    .randomId(null)
                    .build();
        } catch (Exception e) {
            // Catch other errors (e.g. user not found) and return Bad Request status
            System.out.println(e);
            return AuthenticationResponse
                    .builder()
                    .status(false)
                    .statusCode(400)  // Bad Request
                    .statusMessage("Login Failed: " + e.getMessage())
                    .token(null)
                    .randomId(null)
                    .build();
        }
    }
}
