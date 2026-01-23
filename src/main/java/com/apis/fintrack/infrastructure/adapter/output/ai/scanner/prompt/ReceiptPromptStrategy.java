package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.documentpromptv1.ReceiptPromptV1;

public final class ReceiptPromptStrategy implements PromptStrategy {

    @Override
    public boolean supports(String documentType) {
        return "RECEIPT".equalsIgnoreCase(documentType);
    }

    @Override
    public String buildSystemPrompt() {
        return ReceiptPromptV1.value();
    }
}

