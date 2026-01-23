/**
 * Puertos de entrada (primarios) del mÃ³dulo Transaction.
 *
 * Definen los casos de uso disponibles:
 * - CreateTransactionUseCase: Crear nuevas transacciones
 * - FindTransactionUseCase: Buscar transacciones
 * - UpdateTransactionUseCase: Actualizar transacciones existentes
 * - DeleteTransactionUseCase: Eliminar transacciones
 *
 * Reglas de negocio aplicadas:
 * - El valor de las transacciones NO puede ser cero
 * - Los gastos tienen valores NEGATIVOS en el registro
 * - Los ingresos tienen valores POSITIVOS
 * - Las transacciones solo pueden tener UNA categorÃ­a
 */
package com.apis.fintrack.domain.transaction.port.input;


