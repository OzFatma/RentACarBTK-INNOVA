package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.AuthService;
import com.btkAkademi.rentACar.business.requests.LoginRequest;
import com.btkAkademi.rentACar.business.responses.LoginResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        super();
        this.authService = authService;
    }

    @PostMapping("login")
    public DataResult<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return this.authService.login(loginRequest);
    }
}
