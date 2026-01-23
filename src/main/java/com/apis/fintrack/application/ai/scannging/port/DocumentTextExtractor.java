package com.apis.fintrack.application.ai.scannging.port;

import com.apis.fintrack.application.ai.scannging.model.DocumentToScanType;
import com.apis.fintrack.application.ai.scannging.model.DocumentType;

/**
 * Puerto de salida (application layer) para extracción de texto desde documentos.
 * Implementaciones pertenecen a la capa de infraestructura.
 */
public interface DocumentTextExtractor {

    /**
     * Extrae texto plano de un documento representado por bytes.
     *
     * @param content los bytes crudos del documento; puede ser null
     * @param type    tipo del documento; puede ser null
     * @return texto extraído, o cadena vacía si no se pudo extraer o en caso de error
     */
    String extractText(byte[] content, DocumentToScanType type);
}


