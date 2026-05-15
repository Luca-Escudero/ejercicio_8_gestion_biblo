package com.unidad5.ejercicio8.controller;

import com.unidad5.ejercicio8.security.CustomUserDetailsService;
import com.unidad5.ejercicio8.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(CustomUserDetailsService userDetailsService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        userDetailsService.registrarUsuario(request.get("username"), request.get("password"));
        return Map.of("message", "Usuario registrado exitosamente con ROLE_LECTOR");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.get("username"));
        String jwtToken = jwtService.generateToken(user);
        return Map.of("token", jwtToken);
    }
}