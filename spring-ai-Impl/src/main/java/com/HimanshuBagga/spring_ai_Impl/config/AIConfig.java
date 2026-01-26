package com.HimanshuBagga.spring_ai_Impl.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) { // AI connecter
        return builder
                .build();
    }
    // This Create One AI client object for my whole application so that i can use it anywhere to talk to the AI model
}
