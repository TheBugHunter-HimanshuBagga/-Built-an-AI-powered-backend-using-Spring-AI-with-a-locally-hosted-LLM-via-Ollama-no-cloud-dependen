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

@Service //business logic here
@RequiredArgsConstructor // Lombok automatically creates a constructor all final fields
public class AIService {

    private final ChatClient chatClient; //Used to talk to the AI model
    private final EmbeddingModel embeddingModel; // open-ai , converts text into vector -> used for semantic search
    private final VectorStore vectorStore; // VectorStore stores those vector (for RAG / similarity search later)
    // embedding
    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    } // Takes normal text -> Sends it to embedding model -> returns a vector
      // used for semantic search(meaning based search) -> similarity matching(converts text to vectors , converts user questions to vectors , compare vectors , pick most similar once) -> RAG


    public String askAI(String prompt){
        return chatClient.prompt()// starts building a request
                .user(prompt)//send user message to AI
                .call()//actually send request to the model
                .content();// extract only the text reply
    } // Sends prompt to AI and return its answers in String

    public void ingestDataToVectorStore(String text){
        Document document = new Document(text); // wrap text inside document
        vectorStore.add(List.of(document));// Stores it in vectorStore
    } // This is used for searching similar content and answering question from own data

    public String getJoke(String topic){

        String systemPrompt = """ 
                You are a sarcastic joker, yoy make poetic jokes in 4 lines.
                You don't make joke about politics.
                Give a joke on the topic: {topic}
                """; // This is an instruction to AI
        // This prompt is send to an AI
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt); // handels dynamic prompts
        String renderedList = promptTemplate.render(Map.of("topic" , topic)); // replace topic with real value

        var response = chatClient.prompt() // starts building a chat request
                .user(renderedList)//This is the message you send to the AI.
                .advisors(
                       new SimpleLoggerAdvisor()
                )
                .call()//Actually sends the request to the AI and waits for the response.
                .entity(joke.class);//AI will return a JSON convert it to Joke class object
        //
        return response.text();
    }
}
