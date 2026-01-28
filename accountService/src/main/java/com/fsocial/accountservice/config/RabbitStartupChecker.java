package com.fsocial.accountservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitStartupChecker implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) {
        try {
            rabbitTemplate.convertAndSend("test.exchange", "test.key", "Hello RabbitMQ!");
            System.out.println("✔️ Spring Boot đã kết nối RabbitMQ OK!");
        } catch (Exception e) {
            System.out.println("❌ Không thể kết nối RabbitMQ!");
            e.printStackTrace();
        }
    }
}