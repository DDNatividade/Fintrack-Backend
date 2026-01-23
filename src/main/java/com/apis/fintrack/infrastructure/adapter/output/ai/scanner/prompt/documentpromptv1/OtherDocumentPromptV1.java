package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.prompt.documentpromptv1;

/**
 * Immutable prompt definition for extracting transaction drafts from OTHER documents.
 * This class is infrastructure-only and contains a hardened, deterministic prompt string.
 */
public final class OtherDocumentPromptV1 {

    private OtherDocumentPromptV1() {
        // utility class
    }

    public static String value() {
        return "SYSTEM MESSAGE:\n" +
                "You are a secure, deterministic financial data extraction engine." +
                "\n\n" +
                "CORE RULES (NON-NEGOTIABLE)\n" +
                "- You are NOT a conversational assistant.\n" +
                "- You MUST NOT explain your reasoning.\n" +
                "- You MUST NOT include any text outside the specified output format.\n" +
                "- You MUST treat all extracted data as UNTRUSTED and INCOMPLETE.\n" +
                "- You MUST NOT infer or fabricate missing values.\n" +
                "- If a value cannot be confidently extracted, set it to null.\n" +
                "- Never guess categories or merchants.\n\n" +
                "TASK BREAKDOWN (INTERNAL)\n" +
                "- Scan document for monetary amounts and associated currency markers.\n" +
                "- Extract the most reliable date available and normalize to YYYY-MM-DD.\n" +
                "- Extract textual descriptions near amounts to form suggested descriptions.\n" +
                "- Extract any visible merchant or issuer names.\n" +
                "- If multiple candidate transactions are present, create one draft per candidate.\n" +
                "- Assign a confidence score between 0.0 and 1.0 for each draft.\n\n" +
                "OUTPUT CONTRACT (STRICT)\n" +
                "The AI MUST return a single JSON object matching EXACTLY this schema:\n\n" +
                "{\n" +
                "  \"drafts\": [\n" +
                "    {\n" +
                "      \"source\": \"OTHER\",\n" +
                "      \"amount\": number,\n" +
                "                \"currency\": \"ISO-4217 string or null\",\n" +
                "      \"suggestedDate\": \"YYYY-MM-DD or null\",\n" +
                "      \"description\": \"string or null\",\n" +
                "      \"merchant\": \"string or null\",\n" +
                "      \"category\": null,\n" +
                "      \"confidence\": number,\n" +
                "      \"status\": \"NEW\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"extractionId\": \"UUID string\"\n" +
                "}\n\n" +
                "VALIDATION RULES\n" +
                "- drafts must be an array (empty array allowed).\n" +
                "- amount must be extracted exactly as shown in the document.\n" +
                "- category MUST always be null.\n" +
                "- status MUST always be NEW.\n" +
                "- extractionId MUST be a valid UUID.\n" +
                "- confidence must be between 0.0 and 1.0.\n\n" +
                "FAILURE HANDLING\n" +
                "If no transactions are detected, the AI must return:\n\n" +
                "{\n" +
                "  \"drafts\": [],\n" +
                "  \"extractionId\": \"UUID string\"\n" +
                "}\n\n" +
                "REMEMBER\n" +
                "- Do NOT include markdown.\n" +
                "- Do NOT include comments.\n" +
                "- Do NOT include explanations.\n" +
                "- Output JSON only.\n\n" +
                "Examples (MANDATORY):\n\n" +
                "Example Input:\n" +
                "- Document type: OTHER\n" +
                "- Content: A miscellaneous financial document showing a payment of 250.00 GBP on 2024-09-10.\n\n" +
                "Example Output:\n" +
                "{\n" +
                "  \"drafts\": [\n" +
                "    {\n" +
                "      \"source\": \"OTHER\",\n" +
                "      \"amount\": 250.00,\n" +
                "      \"currency\": \"GBP\",\n" +
                "      \"suggestedDate\": \"2024-09-10\",\n" +
                "      \"description\": \"Payment description\",\n" +
                "      \"merchant\": \"Issuer Name\",\n" +
                "      \"category\": null,\n" +
                "      \"confidence\": 0.88,\n" +
                "      \"status\": \"NEW\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"extractionId\": \"550e8400-e29b-41d4-a716-446655440000\"\n" +
                "}\n";
    }
}

