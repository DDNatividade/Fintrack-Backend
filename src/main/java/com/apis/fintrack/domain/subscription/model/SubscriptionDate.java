package com.apis.fintrack.domain.subscription.model;

import lombok.Getter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
public class SubscriptionDate {

    private static final int MONTHS_IN_YEAR = 12;
    private static final int MONTHS_IN_MONTH = 1;

    private final LocalDate value;

    private SubscriptionDate(LocalDate value) {
        this.value = value;
    }

    public static SubscriptionDate of(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("La fecha de suscripciÃ³n no puede ser nula");
        }
        return new SubscriptionDate(value);
    }

    public static SubscriptionDate now() {
        return new SubscriptionDate(LocalDate.now());
    }

    public static SubscriptionDate fromStorage(LocalDate storedDate) {
        if (storedDate == null) {
            throw new IllegalArgumentException("La fecha almacenada no puede ser nula");
        }
        return new SubscriptionDate(storedDate);
    }

    // ==================== MÃ‰TODOS DE EXPIRACIÃ“N ====================

    /**
     * Calcula la fecha de expiraciÃ³n segÃºn el tipo de suscripciÃ³n.
     *
     * @param type el tipo de suscripciÃ³n (MONTHLY o ANNUAL)
     * @return la fecha de expiraciÃ³n
     */
    public LocalDate getExpirationDate(SubscriptionType type) {
        Objects.requireNonNull(type, "El tipo de suscripciÃ³n no puede ser nulo");

        return switch (type) {
            case MONTHLY -> value.plusMonths(MONTHS_IN_MONTH);
            case ANNUAL -> value.plusMonths(MONTHS_IN_YEAR);
        };
    }

    /**
     * Verifica si la suscripciÃ³n ha expirado segÃºn el tipo.
     *
     * @param type el tipo de suscripciÃ³n
     * @return true si ha expirado
     */
    public boolean isExpired(SubscriptionType type) {
        LocalDate expirationDate = getExpirationDate(type);
        return LocalDate.now().isAfter(expirationDate);
    }

    /**
     * Calcula los dÃ­as restantes hasta la expiraciÃ³n.
     *
     * @param type el tipo de suscripciÃ³n
     * @return dÃ­as restantes (negativo si ya expirÃ³)
     */
    public long daysUntilExpiration(SubscriptionType type) {
        LocalDate expirationDate = getExpirationDate(type);
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    /**
     * Verifica si la suscripciÃ³n estÃ¡ prÃ³xima a expirar.
     *
     * @param type el tipo de suscripciÃ³n
     * @param daysThreshold umbral de dÃ­as para considerar "prÃ³ximo a expirar"
     * @return true si expira dentro del umbral de dÃ­as
     */
    public boolean isAboutToExpire(SubscriptionType type, int daysThreshold) {
        long daysRemaining = daysUntilExpiration(type);
        return daysRemaining >= 0 && daysRemaining <= daysThreshold;
    }

    /**
     * Calcula la prÃ³xima fecha de renovaciÃ³n.
     *
     * @param type el tipo de suscripciÃ³n
     * @return la fecha de renovaciÃ³n
     */
    public LocalDate getNextRenewalDate(SubscriptionType type) {
        LocalDate expirationDate = getExpirationDate(type);
        LocalDate today = LocalDate.now();

        // If already expired, renewal would be from today
        if (today.isAfter(expirationDate)) {
            return today;
        }
        return expirationDate;
    }

    /**
     * Calcula cuÃ¡ntos perÃ­odos completos han pasado desde la fecha de suscripciÃ³n.
     *
     * @param type el tipo de suscripciÃ³n
     * @return nÃºmero de perÃ­odos completos
     */
    public long getCompletedPeriods(SubscriptionType type) {
        long monthsBetween = ChronoUnit.MONTHS.between(value, LocalDate.now());
        return switch (type) {
            case MONTHLY -> monthsBetween;
            case ANNUAL -> monthsBetween / MONTHS_IN_YEAR;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionDate subscriptionDate = (SubscriptionDate) o;
        return value.equals(subscriptionDate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

