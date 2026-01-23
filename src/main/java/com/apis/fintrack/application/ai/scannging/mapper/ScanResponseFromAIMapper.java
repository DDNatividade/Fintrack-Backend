package com.apis.fintrack.application.ai.scannging.mapper;

import com.apis.fintrack.application.ai.scannging.model.ScanResult;
import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

public class ScanResponseFromAIMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ScanResponseFromAIMapper() {}

    public ScanResult fromJson(String json) {
        try {
            var root = OBJECT_MAPPER.readTree(json);

            UUID extractionId = UUID.fromString(root.get("extractionId").asText());

            List<DraftTransaction> drafts =
                    OBJECT_MAPPER.readerForListOf(DraftTransaction.class)
                            .readValue(root.get("drafts"));

            return new ScanResult(drafts, extractionId);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid AI response format. Raw response: " + json, e
            );
        }
    }
}
