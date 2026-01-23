package com.apis.fintrack.application.ai.scannging.model;

public record ScanRequest(
        Byte[] content,
        DocumentType documentType
) {}
