/**
 * Puertos Secundarios (Output Ports) del mÃ³dulo User.
 *
 * Definen las interfaces para servicios externos:
 * - UserRepositoryPort: Persistencia de usuarios
 * - PasswordEncoderPort: CodificaciÃ³n de contraseÃ±as
 *
 * Son implementados por los Adaptadores Secundarios (Infrastructure).
 * Son usados por la capa Application.
 *
 * IMPORTANTE: Estas interfaces NO tienen dependencias de frameworks.
 * Son abstracciones puras del dominio que permiten invertir las dependencias.
 */
package com.apis.fintrack.domain.user.port.output;


