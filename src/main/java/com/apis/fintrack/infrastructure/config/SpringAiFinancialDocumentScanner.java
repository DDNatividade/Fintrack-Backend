package com.apis.fintrack.infrastructure.config;

import com.apis.fintrack.application.ai.scannging.mapper.ScanResponseFromAIMapper;
import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.chat.ChatRequest;
import com.apis.fintrack.application.ai.scannging.model.ScanRequest;
import com.apis.fintrack.application.ai.scannging.model.ScanResult;
import com.apis.fintrack.application.ai.scannging.port.DocumentScanningPort;
import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.PromptFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

/**
 * Spring AI adapter implementing DocumentScanningPort using OpenAI.
 */
@Component
public class SpringAiFinancialDocumentScanner implements DocumentScanningPort {

    private final PromptFactory promptFactory;
    private final ScanResponseFromAIMapper mapper;
    private final ChatClient chatClient;

    public SpringAiFinancialDocumentScanner(
            PromptFactory promptFactory,
            ScanResponseFromAIMapper mapper,
            ChatClient chatClient
    ) {
        this.promptFactory = promptFactory;
        this.mapper = mapper;
        this.chatClient = chatClient;
    }

    @Override
    public ScanResult scan(@NonNull ScanRequest request) {
        String systemPrompt = promptFactory.createPrompt(request.documentType().name());

        String userMessage = ChatRequest.of(request.content(), systemPrompt).createMessage();

        String aiResponse = chatClient
                .prompt(systemPrompt + "\n" + userMessage)
                .call()
                .content();

        return mapper.fromJson(aiResponse);
    }

}
