package com.apis.fintrack.infrastructure.adapter.input.rest.mapper;

import com.apis.fintrack.application.ai.scannging.model.DocumentType;
import com.apis.fintrack.application.ai.scannging.model.ScanRequest;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.ScanDocumentRequestDTO;

public final class ScanRequestMapper {

    public ScanRequestMapper() {
    }

    public ScanRequest toApplication(ScanDocumentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Byte[] content = dto.content() == null ? new Byte[0] : dto.content();
        DocumentType documentType = safeDocumentType(dto.documentType());
        return new ScanRequest(content, documentType);
    }

    private DocumentType safeDocumentType(String raw) {
        if (raw == null) {
            return DocumentType.OTHER;
        }
        try {
            return DocumentType.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return DocumentType.OTHER;
        }
    }

}
