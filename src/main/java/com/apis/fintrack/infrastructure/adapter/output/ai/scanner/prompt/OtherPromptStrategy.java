package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.documentpromptv1.OtherDocumentPromptV1;

public final class OtherPromptStrategy implements PromptStrategy {

    @Override
    public boolean supports(String documentType) {
        return "OTHER".equalsIgnoreCase(documentType);
    }

    @Override
    public String buildSystemPrompt() {
        return OtherDocumentPromptV1.value();
    }
}

