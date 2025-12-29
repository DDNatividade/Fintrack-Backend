package com.apis.fintrack.application.ai.scannging.model;

import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;

import java.util.List;
import java.util.UUID;

public record ScanResult(
        List<DraftTransaction> drafts,
        UUID extractionId
) {}
