package com.promotion.auth;


import com.promotion.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "http://localhost:4200")
public class AuthenticationController {
   
    private final  AuthenticationService service;

    @Autowired
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/agent/register")
    public ResponseEntity<RegisterResponse> agentRegister(@RequestHeader("Authorization") String token, @RequestBody RegisterRequest request){
        String jwtToken = token.substring(7);
        String adminRandomId = jwtService.extractUsername(jwtToken);
        return ResponseEntity.ok(service.agentRegister(adminRandomId,request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse resObj = service.authenticate(request);
        if (resObj.getStatusCode() == 200) {
            return ResponseEntity.ok().body(resObj);  // 200 OK
        } else if (resObj.getStatusCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resObj);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resObj);
        }
    }


}
