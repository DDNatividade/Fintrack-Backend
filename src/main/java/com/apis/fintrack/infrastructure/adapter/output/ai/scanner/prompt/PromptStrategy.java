package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

public interface PromptStrategy {

    /**
     * Indicates whether this strategy supports the given document type.
     */
    boolean supports(String documentType);

    /**
     * Builds the SYSTEM prompt for the given document.
     * The content must NOT be embedded directly into the prompt.
     */
    String buildSystemPrompt();
}
