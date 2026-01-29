package com.HimanshuBagga.spring_ai_Impl.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service //business logic here
@RequiredArgsConstructor
public class RAGservice {
    private final ChatClient chatClient; //Used to talk to the AI model
    private final VectorStore vectorStore; // VectorStore stores those vector (for RAG / similarity search later)

    @Value("classpath:faq.pdf")
    Resource pdfFile;
    public String askAI(String prompt){

        String template = """
                You are an ai assistant
                
                RULES
                - You only use the information provided in context
                - you may rephrase, summarise , and explain in natural language
                - You don't introduce new facts or concepts
                - If multiple context refrence are relevant , combine it into a single explanation
                
                Context:
                {context}
                
                Answer in a friendly , conversational tone
                """;
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder())
                .query(prompt)
                .topK(2)
                .similarityThreshold(0.4)
                .filterExpression("file_name == 'faq.pdf'")
                .build();

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        PromptTemplate promptTemplate = new PromptTemplate(context);
        String systemPrompt = promptTemplate.render(Map.of("context" , context));

        return chatClient.prompt()// starts building a request
                .user(prompt)//send user message to AI
                .call()//actually send request to the model
                .content();// extract only the text reply
    }    // Sends prompt to AI and return its answers in String

public void IngestPdftoVectorStore(){
    PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);
    List<Document> pages = reader.get(); // whiole list of pages but has a lot of information

    // Transformer
    TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
            .withChunkSize(200)
            .build();

    List<Document> chunks = tokenTextSplitter.apply(pages);

    // add to vector Store DB
    vectorStore.add(chunks);
}

    public static List<Document> springAiDocs(){
        return List.of(
                new Document(
                        "Spring Ai provides abstraction like chatClient , chat Model and Embedding model to interact with llm",
                        Map.of("topic" , "ai")
                ),
                new Document(
                        "Vectorshift is used to persist embedding and perform similarity search",
                        Map.of("topic" , "vectorstore")
                )
        );
    }
}
