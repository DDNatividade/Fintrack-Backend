package com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit;

import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;

import java.util.*;

public record ScanDocumentResponseDto(UUID extractionId, List<DraftTransaction> drafts) {

    public ScanDocumentResponseDto(UUID extractionId, List<DraftTransaction> drafts) {
        this.extractionId = extractionId;
        if (drafts == null) {
            this.drafts = Collections.emptyList();
        } else {
            this.drafts = List.copyOf(drafts);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScanDocumentResponseDto)) return false;
        ScanDocumentResponseDto that = (ScanDocumentResponseDto) o;
        return Objects.equals(extractionId, that.extractionId) && Objects.equals(drafts, that.drafts);
    }

}
