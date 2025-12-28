package com.apis.fintrack.application.ai.model;

import com.apis.fintrack.application.ai.draft.DraftTransaction;

import java.util.List;
import java.util.UUID;

public record ScanResult(
        List<DraftTransaction> drafts,
        UUID extractionId
) {}
