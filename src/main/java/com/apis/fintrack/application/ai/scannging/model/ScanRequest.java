package com.apis.fintrack.application.ai.scannging.model;

public record ScanRequest(
        byte[] content,
        DocumentType documentType
) {}
