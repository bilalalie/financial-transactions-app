package com.fintech;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FintechApplication {
    public static void main(String[] args) {
        SpringApplication.run(FintechApplication.class, args);
    }
    @Bean
    CommandLineRunner runner() {
        return args -> {
            System.out.println("=======================================");
            System.out.println("🚀 Fintech Application Started!");
            System.out.println("📄 Swagger UI: http://localhost:8080/swagger-ui/index.html");
            System.out.println("=======================================");
        };
    }
}
