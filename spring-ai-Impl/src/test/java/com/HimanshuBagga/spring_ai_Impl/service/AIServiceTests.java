package com.HimanshuBagga.spring_ai_Impl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTests {
    @Autowired
    private AIService aiService;

    @Test
    public void testaskAI(){
        var response = aiService.getJoke("What is Apple");
        System.out.println(response);
    }


    @Test
    public void testGetJoke(){
        var joke = aiService.getJoke("Dogs");
        System.out.println(joke);
    }

    @Test
    public void testEmbedTest(){
        var embed = aiService.getEmbedding("This is a big text here"); // [0.3 , -0.52 , ..........]

        for(float e : embed){
            System.out.println(e + " ");
        }
    }

    @Test
    public void testStoreData(){
        aiService.ingestDataToVectorStore("This is a text");
    }
}
