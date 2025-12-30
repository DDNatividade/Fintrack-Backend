package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.documentpromptv1.InvoicePromptV1;

public final class InvoicePromptStrategy implements PromptStrategy {

    @Override
    public boolean supports(String documentType) {
        return "INVOICE".equalsIgnoreCase(documentType);
    }

    @Override
    public String buildSystemPrompt() {
        return InvoicePromptV1.value();
    }
}

