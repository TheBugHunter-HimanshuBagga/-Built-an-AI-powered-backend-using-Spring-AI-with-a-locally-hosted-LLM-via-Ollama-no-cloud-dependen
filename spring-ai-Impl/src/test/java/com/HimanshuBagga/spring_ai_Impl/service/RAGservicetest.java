package com.HimanshuBagga.spring_ai_Impl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RAGservicetest {

    @Autowired
    private RAGservice ragservice;
    @Test
    public void testIngest(){
        ragservice.IngestPdftoVectorStore();
    }
    @Test
    public void testAskAi(){
        var response = ragservice.askAI("What is Apple");
        System.out.println(response);
    }
}
