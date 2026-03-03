package com.fintech.controller;

import com.fintech.dto.AuthDto;
import com.fintech.model.Account;
import com.fintech.repository.AccountRepository;
import com.fintech.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request) {
        try {
            /*
             * 1. THE CHALLENGE
             * Pass the username and password to the AuthenticationManager.
             * This triggers the DaoAuthenticationProvider to check the
             * password against what your UserDetailsService found in the DB.
             */
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // 2. FAIL: If password/username is wrong, return 401 Unauthorized
            return ResponseEntity.status(401).body("Invalid username or password");
        }
            /*
             * 3. SUCCESS: If we reach here, the user is authenticated.
             * Now we fetch the user details to generate their "passport" (the JWT).
             */
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        // Fetch the full account entity to return extra metadata (balance, name, etc.)
        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow();

        /*
         * 4. THE RESPONSE
         * We send back the JWT plus useful UI info so the frontend doesn't
         * have to make a second call immediately just to show the user's name/balance.
         */
        return ResponseEntity.ok(new AuthDto.LoginResponse(
                token,
                account.getUsername(),
                account.getFullName(),
                account.getId(),
                account.getBalance()
        ));
    }
}
