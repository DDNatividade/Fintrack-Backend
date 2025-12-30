package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt;

import com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.documentpromptv1.BankStatementPromptV1;

public final class BankStatementPromptStrategy implements PromptStrategy {

    @Override
    public boolean supports(String documentType) {
        return "BANK_STATEMENT".equalsIgnoreCase(documentType);
    }

    @Override
    public String buildSystemPrompt() {
        return BankStatementPromptV1.value();
    }
}
