package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry;

import java.util.Arrays;
import java.util.Objects;

public record ScanDocumentRequestDTO(Byte[] content, String documentType) {

    public ScanDocumentRequestDTO(Byte[] content, String documentType) {
        this.content = content == null ? null : content.clone();
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScanDocumentRequestDTO)) return false;
        ScanDocumentRequestDTO that = (ScanDocumentRequestDTO) o;
        return Arrays.equals(content, that.content) && Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(content);
        result = 31 * result + (documentType != null ? documentType.hashCode() : 0);
        return result;
    }

}
