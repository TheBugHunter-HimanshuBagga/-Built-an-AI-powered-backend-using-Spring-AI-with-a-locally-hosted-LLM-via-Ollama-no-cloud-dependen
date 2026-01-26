package com.HimanshuBagga.spring_ai_Impl.service;

import com.HimanshuBagga.spring_ai_Impl.dto.joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel; // open-ai
    private final VectorStore vectorStore;
    // embedding
    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    }

    public void ingestDataToVectorStore(String text){
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public String getJoke(String topic){

        String systemPrompt = """ 
                You are a sarcastic joker, yoy make poetic jokes in 4 lines.
                You don't make joke about politics.
                Give a joke on the topic: {topic}
                """;
        // This prompt is send to an AI
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt); // handels dynamic prompts
        String renderedList = promptTemplate.render(Map.of("topic" , topic));

        var response = chatClient.prompt() // starts building a chat request
                .user(renderedList)//This is the message you send to the AI.
                .advisors(
                       new SimpleLoggerAdvisor()
                )
                .call()//Actually sends the request to the AI and waits for the response.
                .entity(joke.class);//Maps the response JSON to your joke DTO (data object).
        //
        return response.text();
    }
}
