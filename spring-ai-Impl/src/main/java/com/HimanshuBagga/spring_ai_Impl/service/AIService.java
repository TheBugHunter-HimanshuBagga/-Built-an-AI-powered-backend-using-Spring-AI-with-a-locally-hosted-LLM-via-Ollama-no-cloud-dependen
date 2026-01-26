package com.HimanshuBagga.spring_ai_Impl.service;

import com.HimanshuBagga.spring_ai_Impl.dto.joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;

    public String getJoke(String topic){

        String systemPrompt = """
                You are a sarcastic joker, yoy make poetic jokes in 4 lines.
                You don't make joke about politics.
                Give a joke on the topic: {topic}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String renderedList = promptTemplate.render(Map.of("topic" , topic));

        var response = chatClient.prompt()
                .user(renderedList)
                .advisors(
                       new SimpleLoggerAdvisor()
                )
                .call()
                .entity(joke.class);
        return response.text();
    }
}
