package com.apis.fintrack.application.ai.scannging.port;

import com.apis.fintrack.application.ai.scannging.model.ScanRequest;
import com.apis.fintrack.application.ai.scannging.model.ScanResult;

/**
 * Primary port (application layer) para solicitar a un adaptador de IA
 * que escanee un documento binario y devuelva borradores (DraftTransaction)
 * extraídos automáticamente. Esta interfaz es vendor-agnostic y pertenece
 * a la capa de aplicación; implementaciones concretas vivirán en infraestructura.
 */
public interface DocumentScanningPort {

    ScanResult scan(ScanRequest request);

}


