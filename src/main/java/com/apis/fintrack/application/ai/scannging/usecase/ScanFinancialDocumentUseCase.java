package com.apis.fintrack.application.ai.scannging.usecase;

import com.apis.fintrack.application.ai.scannging.draft.DraftStatus;
import com.apis.fintrack.application.ai.scannging.draft.DraftTransaction;
import com.apis.fintrack.application.ai.scannging.mapper.ScanResponseFromAIMapper;
import com.apis.fintrack.application.ai.scannging.model.ScanRequest;
import com.apis.fintrack.application.ai.scannging.model.ScanResult;
import com.apis.fintrack.application.ai.scannging.port.DocumentScanningPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Caso de uso de la capa de aplicación que orquesta el flujo de escaneo de documentos financieros.
 *
 * - Invoca el puerto DocumentScanningPort para realizar el escaneo (infraestructura).
 * - Marca todos los borradores devueltos con DraftStatus.NEW sin mutar objetos existentes.
 * - NO persiste los borradores; permanecen como datos no confiables hasta confirmación del usuario.
 */
public final class ScanFinancialDocumentUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanFinancialDocumentUseCase.class);

    private final DocumentScanningPort documentScanningPort;

    public ScanFinancialDocumentUseCase(DocumentScanningPort documentScanningPort, ScanResponseFromAIMapper mapper) {
        this.documentScanningPort = Objects.requireNonNull(documentScanningPort, "documentScanningPort must not be null");
    }

    /**
     * Orquesta el escaneo del documento indicado en el request.
     *
     * @param request petición de escaneo; no puede ser null
     * @return ScanResult con la lista de borradores marcada como DraftStatus.NEW y el extractionId
     * @throws IllegalArgumentException si request es null
     */
    public ScanResult scanFinancialDocument(ScanRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        LOGGER.debug("scanFinancialDocument invoked for documentType={}", Objects.toString(request.documentType(), "null"));

        ScanResult scanResult;
        try {
            scanResult = documentScanningPort.scan(request);
        } catch (Exception e) {
            LOGGER.error("Error invoking DocumentScanningPort.scan: {}", e.getMessage(), e);
            return buildEmptyScanResult();
        }

        if (scanResult == null || scanResult.drafts() == null) {
            LOGGER.warn("DocumentScanningPort.scan returned null; returning empty ScanResult");
            return buildEmptyScanResult();
        }

        List<DraftTransaction> draftsWithStatusNew =
                scanResult.drafts().stream()
                        .filter(Objects::nonNull)
                        .map(draft -> draft.withStatus(DraftStatus.NEW))
                .collect(Collectors.toList());


        return new ScanResult(draftsWithStatusNew, scanResult.extractionId());
    }

    private ScanResult buildEmptyScanResult() {
        return new ScanResult(Collections.emptyList(), null);
    }
}
