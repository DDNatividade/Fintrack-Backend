package com.apis.fintrack.infrastructure.config;

import com.apis.fintrack.application.ai.scannging.mapper.ScanResponseMapper;
import com.apis.fintrack.application.ai.scannging.model.ScanRequest;
import com.apis.fintrack.application.ai.scannging.model.ScanResult;
import com.apis.fintrack.application.ai.scannging.port.DocumentScanningPort;
import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.PromptFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;


import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Spring AI adapter implementing DocumentScanningPort using OpenAI.
 */
@Component
public class SpringAiFinancialDocumentScanner implements DocumentScanningPort {

    private final PromptFactory promptFactory;
    private final ScanResponseMapper mapper;
    private final ChatClient chatClient;

    public SpringAiFinancialDocumentScanner(
            PromptFactory promptFactory,
            ScanResponseMapper mapper,
            ChatClient chatClient
    ) {
        this.promptFactory = promptFactory;
        this.mapper = mapper;
        this.chatClient = chatClient;
    }

    @Override
    public ScanResult scan(@NonNull ScanRequest request) {
        // 1. Resolve system prompt using PromptFactory
        String systemPrompt = promptFactory.createPrompt(request.documentType().name());

        // 2. Preprocess document safely to extract text
        String userPrompt = extractTextFromDocument(request.content());

        // 3. Build messages for OpenAI Chat API
        List<ChatMessage> messages = List.of(
                ChatMessage.ofSystem(systemPrompt),
                ChatMessage.ofUser(userPrompt)
        );

        // 4. Create chat request
        OpenAiApi.ChatCompletionRequest completionRequest = OpenAiApi.ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-4") // or "gpt-3.5-turbo"
                .build();

        // 5. Call OpenAI via Spring AI OpenAIChatClient
        ChatCompletionResponse response = chatClient.chat(completionRequest);

        // 6. Map AI JSON response to ScanResult
        String aiContent = response.choices().get(0).message().content();
        return mapper.toScanResult(aiContent, request);
    }

    /**
     * Converts raw document bytes into safe, textual content for AI consumption.
     * Replace this prototype with OCR/PDF parser as needed.
     */
    private String extractTextFromDocument(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        return new String(content, StandardCharsets.UTF_8);
    }
}
