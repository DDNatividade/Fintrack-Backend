/**
 * Paquete de AnÃ¡lisis Financiero - Arquitectura Hexagonal
 *
 * Estructura de carpetas:
 * - model/           : Entidades y modelos del dominio (AnalysisResult, AnalysisType)
 * - Port/Input       : Puertos de entrada (casos de uso)
 * - strategy/        : Estrategias de anÃ¡lisis (implementan la lÃ³gica especÃ­fica)
 * - service/         : Adaptadores que implementan los puertos
 *
 * Flujo de datos (Arquitectura Hexagonal):
 * Controlador â†’ IFinancialAnalysisUseCase (Puerto)
 *            â†’ FinancialAnalysisService (Adaptador)
 *            â†’ FinancialAnalysisStrategy (Estrategias concretas)
 *            â†’ Modelos de dominio
 *
 * Responsabilidades:
 * - model: RepresentaciÃ³n de datos del anÃ¡lisis
 * - Port: Contrato de los casos de uso
 * - strategy: Algoritmos especÃ­ficos de anÃ¡lisis (patrÃ³n Strategy)
 * - service: OrquestaciÃ³n e inyecciÃ³n de dependencias
 */
package com.apis.fintrack.domain.Analysis;


