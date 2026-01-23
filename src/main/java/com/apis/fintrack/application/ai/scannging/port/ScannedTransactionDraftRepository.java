package com.apis.fintrack.application.ai.scannging.port;

import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;
import java.util.List;
import java.util.UUID;

public interface ScannedTransactionDraftRepository {

    void saveAll(List<DraftTransaction> drafts);

    List<DraftTransaction> findByScanId(UUID scanId);
}
