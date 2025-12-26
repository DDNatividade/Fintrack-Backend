package com.apis.fintrack.domain.payment.port.output;

import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.subscription.model.Subscription;

import java.util.List;

/**
 * Puerto de salida para integración con servicios de pago.
 */
public interface PaymentServicePort {

    /**
     * Valida que el método de pago sea aceptado por el proveedor externo.
     */
    boolean validatePaymentMethod(PaymentMethod paymentMethod);

    /**
     * Obtiene pagos pendientes asociados a una suscripción.
     */
    List<Payment> findPendingPayments(Subscription subscription);
}
