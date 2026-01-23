package com.apis.fintrack.infrastructure.adapter.output.ocr;

import com.apis.fintrack.application.ai.scannging.model.DocumentToScanType;
import com.apis.fintrack.application.ai.scannging.port.DocumentTextExtractor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Adaptador de infraestructura (adapter.output.ocr) responsable de convertir documentos en texto plano.
 *
 * - PDF: Apache PDFBox
 * - IMAGE (JPG, PNG, etc.): Tesseract OCR (Tess4J)
 * - OTHER: no soportado, devuelve cadena vacía y registra advertencia
 *
 * Esta clase es final, sin estado y sigue el principio de hexagonal architecture: implementa el puerto
 * de la capa de aplicación sin introducir lógica de dominio ni persistencia.
 *
 * En caso de error devuelve cadena vacía y registra el incidente. No registra ni persiste contenido del documento.
 */
public final class OcrDocumentTextExtractorAdapter implements DocumentTextExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OcrDocumentTextExtractorAdapter.class);
    private static final Tesseract TESSERACT = new Tesseract();

    public OcrDocumentTextExtractorAdapter() {
        // Configuraciones opcionales de Tesseract pueden realizarse aquí, si se necesita.
    }

    @Override
    public String extractText(byte[] content, DocumentToScanType type) {
        if (content == null || content.length == 0 || type == null) {
            return "";
        }
        try {
            return switch (type) {
                case PDF -> extractTextFromPdf(content);
                case IMAGE -> extractTextFromImage(content);
                default -> {
                    LOGGER.warn("Document type OTHER or unsupported; returning empty text.");
                    yield "";
                }
            };
        } catch (Exception e) {
            LOGGER.error("Text extraction failed for document type {}: {}", type, e.getMessage(), e);
            return "";
        }
    }

    private String extractTextFromPdf(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content);
             PDDocument document = Loader.loadPDF(content);
) {

            if (document.isEncrypted()) {
                LOGGER.warn("PDF is encrypted; extraction may produce incomplete results.");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text == null ? "" : text;
        } catch (IOException e) {
            LOGGER.error("PDF text extraction failed: {}", e.getMessage(), e);
            return "";
        }
    }

    private String extractTextFromImage(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
            BufferedImage image = ImageIO.read(bais);
            if (image == null) {
                LOGGER.warn("ImageIO could not decode image bytes; returning empty text.");
                return "";
            }
            try {
                String result = TESSERACT.doOCR(image);
                return result == null ? "" : result;
            } catch (TesseractException te) {
                LOGGER.error("Tesseract OCR failed: {}", te.getMessage(), te);
                return "";
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read image bytes: {}", e.getMessage(), e);
            return "";
        }
    }




}