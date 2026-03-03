package com.fintech.config;

import com.fintech.model.Account;
import com.fintech.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!accountRepository.existsByUsername("alice")) {
                Account alice = new Account();
                alice.setUsername("alice");
                alice.setPassword(passwordEncoder.encode("alice123"));
                alice.setFullName("Alice Johnson");
                alice.setBalance(new BigDecimal("10000.00"));
                alice.setRole("USER");
                accountRepository.save(alice);
            }

            if (!accountRepository.existsByUsername("bob")) {
                Account bob = new Account();
                bob.setUsername("bob");
                bob.setPassword(passwordEncoder.encode("bob123"));
                bob.setFullName("Bob Smith");
                bob.setBalance(new BigDecimal("5000.00"));
                bob.setRole("USER");
                accountRepository.save(bob);
            }

            System.out.println("=== Accounts initialized ===");
            System.out.println("Account 1: username=alice, password=alice123, balance=$10,000");
            System.out.println("Account 2: username=bob, password=bob123, balance=$5,000");
        };
    }
}
