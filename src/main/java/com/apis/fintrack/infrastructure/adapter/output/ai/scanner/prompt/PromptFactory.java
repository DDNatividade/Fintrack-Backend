package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

import java.util.List;

public final class PromptFactory {
    private final List<PromptStrategy> strategies;

    public PromptFactory(List<PromptStrategy> strategies) {
        this.strategies = strategies;
    }

    public String createPrompt(String documentType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(documentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No prompt strategy found for document type: " + documentType))
                .buildSystemPrompt();
    }
}
