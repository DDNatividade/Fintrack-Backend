package com.apis.fintrack.application.ai.model;

public record ScanRequest(
        byte[] content,
        DocumentType documentType
) {}
