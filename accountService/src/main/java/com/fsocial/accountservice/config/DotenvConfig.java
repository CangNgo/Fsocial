//package com.fsocial.accountservice.config;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DotenvConfig {
//    @Bean
//    public Dotenv dotenv() {
//        Dotenv dotenv = Dotenv.configure()
//                .directory("/fsocial")
//                .filename("production.env")
//                .load();
//        System.out.println("AUTHEN_SERVICE_URL: " + dotenv.get("AUTHEN_SERVICE_URL"));
//        return dotenv;
//    }
//}
