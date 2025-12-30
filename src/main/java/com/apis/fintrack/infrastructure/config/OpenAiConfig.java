package com.apis.fintrack.infrastructure.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig  {
    @Bean
    public CommandLineRunner runner(ChatClient.Builder builder) {
        return builder.build();

    }
}
