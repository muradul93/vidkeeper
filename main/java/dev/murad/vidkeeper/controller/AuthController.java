
package dev.murad.vidkeeper.controller;

import dev.murad.vidkeeper.controller.dto.*;
import dev.murad.vidkeeper.config.JwtUtil;
import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;

    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException e) {
            throw e;
        }

        String token = JwtUtil.generateToken(request.username());
        String role = userService.findByUserName(request.username()).getRole().name(); // Fetch the role from the user service
        return ResponseEntity.ok(new LoginResponse(request.username(), role, token));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String token) {
        String username = JwtUtil.extractUsername(token.substring(7));
        User user = userService.findByUserName(username);
        return ResponseEntity.ok(user);
    }


}