package com.apis.fintrack.infrastructure.adapter.input.rest;

import com.apis.fintrack.application.ai.scannging.usecase.ScanFinancialDocumentUseCase;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ScanDocumentResponseDto;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.ScanDocumentRequestDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.ScanRequestMapper;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.ScanResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

/**
 * REST controller in the Infrastructure layer (adapter.input.rest).
 * Responsibility:
 *  - Expose HTTP endpoint to scan documents.
 *  - Perform basic input validation.
 *  - Map DTOs ↔ application models.
 *  - Invoke the ScanFinancialDocumentUseCase.
 *
 * This controller does not contain domain or infrastructure logic, nor does it perform persistence.
 */
@RestController
@RequestMapping("/api/v1/documents")
public class ScanFinancialDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ScanFinancialDocumentController.class);

    private final ScanFinancialDocumentUseCase scanFinancialDocumentUseCase;
    private final ScanRequestMapper scanRequestMapper;
    private final ScanResponseMapper scanResponseMapper;

    public ScanFinancialDocumentController(
            ScanFinancialDocumentUseCase scanFinancialDocumentUseCase,
            ScanRequestMapper scanRequestMapper,
            ScanResponseMapper scanResponseMapper) {
        this.scanFinancialDocumentUseCase = Objects.requireNonNull(scanFinancialDocumentUseCase, "scanFinancialDocumentUseCase");
        this.scanRequestMapper = Objects.requireNonNull(scanRequestMapper, "scanRequestMapper");
        this.scanResponseMapper = scanResponseMapper;
    }

    /**
     * Endpoint to scan documents.
     *
     * Rules:
     *  - If the request is invalid -> 400 Bad Request
     *  - If the use case returns null -> 204 No Content
     *  - If an unexpected error occurs -> 500 Internal Server Error (no internal details exposed)
     *
     * Note: Document content is not logged or exposed.
     */
    @PostMapping("/scan")
    public ResponseEntity<ScanDocumentResponseDto> scanDocument(@RequestBody ScanDocumentRequestDTO request) {
        try {
            if (request == null) {
                LOG.warn("Received null scan request");
                return ResponseEntity.badRequest().build();
            }

            Byte[] content = request.content();
            if (content == null || content.length == 0) {
                LOG.warn("Invalid scan request: empty content (length={})", content == null ? 0 : content.length);
                return ResponseEntity.badRequest().build();
            }

            String documentType = request.documentType();
            if (!StringUtils.hasText(documentType)) {
                LOG.warn("Invalid scan request: missing or empty documentType");
                return ResponseEntity.badRequest().build();
            }

            var appRequest = scanRequestMapper.toApplication(request);
            if (appRequest == null) {
                LOG.warn("Mapping request to application model returned null");
                return ResponseEntity.badRequest().build();
            }

            var result = scanFinancialDocumentUseCase.scanFinancialDocument(appRequest);

            ScanDocumentResponseDto responseDto = scanResponseMapper.toDto(result);
            return ResponseEntity.ok(responseDto);

        } catch (Exception ex) {
            LOG.error("Unexpected error while processing scan request", ex);
            return ResponseEntity.status(500).build();
        }
    }
}

