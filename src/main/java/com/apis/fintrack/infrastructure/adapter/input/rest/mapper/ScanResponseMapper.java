package com.apis.fintrack.infrastructure.adapter.input.rest.mapper;

import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ScanDocumentResponseDto;
import com.apis.fintrack.application.ai.scannging.model.ScanResult;
import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Mapper from application ScanResult to infrastructure ScanDocumentResponseDto.
 * This mapper is part of the input REST adapter and performs a single responsibility:
 * convert application models to transport DTOs. It contains no domain logic and is defensive
 * against null inputs.
 */
public final class ScanResponseMapper {

    public ScanResponseMapper() {
    }

    public ScanDocumentResponseDto toDto(ScanResult result) {
        if (result == null) {
            return new ScanDocumentResponseDto(null, Collections.emptyList());
        }

        UUID extractionId;
        try {
            extractionId = Objects.requireNonNull(result.extractionId());
        } catch (Exception e) {
            extractionId = null;
        }

        List<DraftTransaction> drafts = result.drafts() == null ? Collections.emptyList() : List.copyOf(result.drafts());

        return new ScanDocumentResponseDto(extractionId, drafts);
    }
}

