package com.fintech.config;

import com.fintech.model.Account;
import com.fintech.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {
    private final AccountRepository accountRepository;

    @Autowired
    public UserDetailsConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // This is a Functional Interface. The 'username' is what the user typed in the login form.
        return username -> {

            // 1. Database Lookup:
            // Attempt to find the account in your DB via the repository.
            Account account = accountRepository.findByUsername(username)
                    // If not found, throw this specific Spring Security exception.
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // 2. Mapping to UserDetails:
            // We take your 'Account' object and convert it into a 'User' object
            // that Spring Security understands.
            return org.springframework.security.core.userdetails.User
                    .withUsername(account.getUsername()) // Set the username
                    .password(account.getPassword())     // Set the hashed password (to be compared later)
                    .roles(account.getRole())            // Assign roles (e.g., "USER", "ADMIN")
                    .build();
        };
    }
}
