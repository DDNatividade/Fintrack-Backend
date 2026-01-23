package com.apis.fintrack.domain.analysis.model.kpi;

import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AnalysisResult.
 *
 * Tests follow specification-based testing approach, focusing on:
 * - Valid and invalid partitions
 * - Boundary conditions
 * - Edge cases
 */
@DisplayName("AnalysisResult Tests")
class AnalysisResultTest {

    private AnalysisType validType;
    private AnalysisMetric validValue;

    @BeforeEach
    void setUp() {
        validType = AnalysisType.MONTHLY_AVERAGE_EXPENSES;
        validValue = new ScalarMetric(new BigDecimal("100.00"), MetricUnit.CURRENCY);
    }

    // ========================================
    // Factory Method: of()
    // ========================================

    @Nested
    @DisplayName("Factory method of()")
    class OfMethodTests {

        @Test
        @DisplayName("Creates result with valid type and value")
        void of_withValidTypeAndValue_createsResult() {
            AnalysisResult result = AnalysisResult.of(validType, validValue);

            assertNotNull(result);
            assertEquals(validType, result.getType());
            assertEquals(validValue, result.getValue());
        }

        @Test
        @DisplayName("Throws exception when type is null")
        void of_withNullType_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.of(null, validValue));
        }

        @Test
        @DisplayName("Throws exception when value is null")
        void of_withNullValue_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.of(validType, null));
        }

        @Test
        @DisplayName("Sets calculatedAt to today's date")
        void of_setsCalculatedAtToToday() {
            AnalysisResult result = AnalysisResult.of(validType, validValue);

            assertEquals(LocalDate.now(), result.getCalculatedAt());
        }

        @Test
        @DisplayName("Creates result without breakdown or reference")
        void of_createsResultWithoutBreakdownOrReference() {
            AnalysisResult result = AnalysisResult.of(validType, validValue);

            assertNull(result.getBreakdown());
            assertNull(result.getReferenceValue());
            assertFalse(result.hasBreakdown());
            assertFalse(result.hasReferenceValue());
        }
    }

    // ========================================
    // Factory Method: withBreakdown()
    // ========================================

    @Nested
    @DisplayName("Factory method withBreakdown()")
    class WithBreakdownMethodTests {

        @Test
        @DisplayName("Creates result with valid breakdown map")
        void withBreakdown_withValidMap_createsResult() {
            Map<String, BigDecimal> breakdown = Map.of(
                "FOOD", new BigDecimal("150.00"),
                "TRANSPORT", new BigDecimal("50.00")
            );

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertNotNull(result);
            assertEquals(breakdown, result.getBreakdown());
            assertTrue(result.hasBreakdown());
        }

        @Test
        @DisplayName("Throws exception when type is null")
        void withBreakdown_withNullType_throwsNullPointerException() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", BigDecimal.TEN);

            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withBreakdown(null, validValue, breakdown));
        }

        @Test
        @DisplayName("Throws exception when value is null")
        void withBreakdown_withNullValue_throwsNullPointerException() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", BigDecimal.TEN);

            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withBreakdown(validType, null, breakdown));
        }

        @Test
        @DisplayName("Throws exception when breakdown is null")
        void withBreakdown_withNullBreakdown_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withBreakdown(validType, validValue, null));
        }

        @Test
        @DisplayName("Creates result with empty map - hasBreakdown returns false")
        void withBreakdown_withEmptyMap_hasBreakdownReturnsFalse() {
            Map<String, BigDecimal> emptyBreakdown = Map.of();

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, emptyBreakdown);

            assertNotNull(result);
            assertNotNull(result.getBreakdown());
            assertTrue(result.getBreakdown().isEmpty());
            assertFalse(result.hasBreakdown());
        }

        @Test
        @DisplayName("Preserves all breakdown entries")
        void withBreakdown_preservesMapContents() {
            Map<String, BigDecimal> breakdown = Map.of(
                "FOOD", new BigDecimal("100.00"),
                "TRANSPORT", new BigDecimal("75.50"),
                "ENTERTAINMENT", new BigDecimal("200.00")
            );

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertEquals(3, result.getBreakdown().size());
            assertEquals(new BigDecimal("100.00"), result.getBreakdown().get("FOOD"));
            assertEquals(new BigDecimal("75.50"), result.getBreakdown().get("TRANSPORT"));
            assertEquals(new BigDecimal("200.00"), result.getBreakdown().get("ENTERTAINMENT"));
        }

        @Test
        @DisplayName("Creates result with single entry breakdown")
        void withBreakdown_withSingleEntry_createsResult() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", new BigDecimal("50.00"));

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertTrue(result.hasBreakdown());
            assertEquals(1, result.getBreakdown().size());
        }
    }

    // ========================================
    // Factory Method: withReference()
    // ========================================

    @Nested
    @DisplayName("Factory method withReference()")
    class WithReferenceMethodTests {

        @Test
        @DisplayName("Creates result with valid reference value")
        void withReference_withValidValue_createsResult() {
            BigDecimal referenceValue = new BigDecimal("1000.00");

            AnalysisResult result = AnalysisResult.withReference(validType, validValue, referenceValue);

            assertNotNull(result);
            assertEquals(referenceValue, result.getReferenceValue());
            assertTrue(result.hasReferenceValue());
        }

        @Test
        @DisplayName("Throws exception when type is null")
        void withReference_withNullType_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withReference(null, validValue, BigDecimal.TEN));
        }

        @Test
        @DisplayName("Throws exception when value is null")
        void withReference_withNullValue_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withReference(validType, null, BigDecimal.TEN));
        }

        @Test
        @DisplayName("Throws exception when reference value is null")
        void withReference_withNullReference_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> AnalysisResult.withReference(validType, validValue, null));
        }

        @Test
        @DisplayName("Creates result with ZERO reference value")
        void withReference_withZero_createsResult() {
            AnalysisResult result = AnalysisResult.withReference(validType, validValue, BigDecimal.ZERO);

            assertNotNull(result);
            assertEquals(BigDecimal.ZERO, result.getReferenceValue());
            assertTrue(result.hasReferenceValue());
        }

        @Test
        @DisplayName("Creates result with negative reference value")
        void withReference_withNegativeValue_createsResult() {
            BigDecimal negativeValue = new BigDecimal("-500.00");

            AnalysisResult result = AnalysisResult.withReference(validType, validValue, negativeValue);

            assertNotNull(result);
            assertEquals(negativeValue, result.getReferenceValue());
        }
    }

    // ========================================
    // Builder Tests
    // ========================================

    @Nested
    @DisplayName("Builder")
    class BuilderTests {

        @Test
        @DisplayName("Creates result with both breakdown and reference")
        void builder_withBothBreakdownAndReference_createsResult() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", new BigDecimal("100.00"));
            BigDecimal referenceValue = new BigDecimal("500.00");

            AnalysisResult result = new AnalysisResult.Builder(validType, validValue)
                    .breakdown(breakdown)
                    .referenceValue(referenceValue)
                    .build();

            assertNotNull(result);
            assertTrue(result.hasBreakdown());
            assertTrue(result.hasReferenceValue());
            assertEquals(breakdown, result.getBreakdown());
            assertEquals(referenceValue, result.getReferenceValue());
        }

        @Test
        @DisplayName("Throws exception when builder constructor receives null type")
        void builder_withNullTypeInConstructor_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> new AnalysisResult.Builder(null, validValue));
        }

        @Test
        @DisplayName("Throws exception when builder constructor receives null value")
        void builder_withNullValueInConstructor_throwsNullPointerException() {
            assertThrows(NullPointerException.class,
                () -> new AnalysisResult.Builder(validType, null));
        }

        @Test
        @DisplayName("Throws exception when breakdown method receives null")
        void builder_breakdownMethodWithNull_throwsNullPointerException() {
            AnalysisResult.Builder builder = new AnalysisResult.Builder(validType, validValue);

            assertThrows(NullPointerException.class,
                () -> builder.breakdown(null));
        }

        @Test
        @DisplayName("Throws exception when referenceValue method receives null")
        void builder_referenceValueMethodWithNull_throwsNullPointerException() {
            AnalysisResult.Builder builder = new AnalysisResult.Builder(validType, validValue);

            assertThrows(NullPointerException.class,
                () -> builder.referenceValue(null));
        }

        @Test
        @DisplayName("Multiple builds produce independent results")
        void builder_calledMultipleTimes_producesIndependentResults() {
            AnalysisResult.Builder builder = new AnalysisResult.Builder(validType, validValue);

            AnalysisResult result1 = builder.build();
            AnalysisResult result2 = builder.build();

            assertNotNull(result1);
            assertNotNull(result2);
            assertNotSame(result1, result2);
        }

        @Test
        @DisplayName("Builder methods can be chained")
        void builder_methodsCanBeChained() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", BigDecimal.TEN);
            BigDecimal reference = new BigDecimal("100.00");

            AnalysisResult result = new AnalysisResult.Builder(validType, validValue)
                    .breakdown(breakdown)
                    .referenceValue(reference)
                    .build();

            assertNotNull(result);
            assertEquals(validType, result.getType());
            assertEquals(validValue, result.getValue());
        }
    }

    // ========================================
    // Query Methods: hasBreakdown() / hasReferenceValue()
    // ========================================

    @Nested
    @DisplayName("Query methods")
    class QueryMethodsTests {

        @Test
        @DisplayName("hasBreakdown returns false when breakdown is null")
        void hasBreakdown_withNull_returnsFalse() {
            AnalysisResult result = AnalysisResult.of(validType, validValue);

            assertFalse(result.hasBreakdown());
        }

        @Test
        @DisplayName("hasBreakdown returns false when breakdown is empty")
        void hasBreakdown_withEmptyMap_returnsFalse() {
            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, Map.of());

            assertFalse(result.hasBreakdown());
        }

        @Test
        @DisplayName("hasBreakdown returns true when breakdown has entries")
        void hasBreakdown_withEntries_returnsTrue() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", BigDecimal.TEN);
            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertTrue(result.hasBreakdown());
        }

        @Test
        @DisplayName("hasReferenceValue returns false when reference is null")
        void hasReferenceValue_withNull_returnsFalse() {
            AnalysisResult result = AnalysisResult.of(validType, validValue);

            assertFalse(result.hasReferenceValue());
        }

        @Test
        @DisplayName("hasReferenceValue returns true when reference is ZERO")
        void hasReferenceValue_withZero_returnsTrue() {
            AnalysisResult result = AnalysisResult.withReference(validType, validValue, BigDecimal.ZERO);

            assertTrue(result.hasReferenceValue());
        }

        @Test
        @DisplayName("hasReferenceValue returns true when reference has value")
        void hasReferenceValue_withValue_returnsTrue() {
            AnalysisResult result = AnalysisResult.withReference(validType, validValue, new BigDecimal("100.00"));

            assertTrue(result.hasReferenceValue());
        }
    }

    // ========================================
    // Immutability Tests
    // ========================================

    @Nested
    @DisplayName("Immutability")
    class ImmutabilityTests {

        @Test
        @DisplayName("Modifying original map does not affect result breakdown")
        void breakdownMap_modifyingOriginal_doesNotAffectResult() {
            Map<String, BigDecimal> originalMap = new HashMap<>();
            originalMap.put("FOOD", new BigDecimal("100.00"));

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, originalMap);

            // Modify original map after creation
            originalMap.put("TRANSPORT", new BigDecimal("50.00"));
            originalMap.remove("FOOD");

            // Result should still have original value
            // Note: This test may fail if the implementation doesn't copy the map
            assertEquals(1, result.getBreakdown().size());
            assertTrue(result.getBreakdown().containsKey("FOOD"));
        }

        @Test
        @DisplayName("Result fields are accessible via getters")
        void getters_returnCorrectValues() {
            Map<String, BigDecimal> breakdown = Map.of("FOOD", BigDecimal.TEN);
            BigDecimal reference = new BigDecimal("100.00");

            AnalysisResult result = new AnalysisResult.Builder(validType, validValue)
                    .breakdown(breakdown)
                    .referenceValue(reference)
                    .build();

            assertEquals(validType, result.getType());
            assertEquals(validValue, result.getValue());
            assertEquals(breakdown, result.getBreakdown());
            assertEquals(reference, result.getReferenceValue());
            assertNotNull(result.getCalculatedAt());
        }
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Nested
    @DisplayName("Edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Works with all AnalysisType values")
        void of_worksWithAllAnalysisTypes() {
            for (AnalysisType type : AnalysisType.values()) {
                AnalysisResult result = AnalysisResult.of(type, validValue);
                assertEquals(type, result.getType());
            }
        }

        @Test
        @DisplayName("Breakdown with negative values is allowed")
        void withBreakdown_withNegativeValues_isAllowed() {
            Map<String, BigDecimal> breakdown = Map.of(
                "REFUND", new BigDecimal("-50.00"),
                "EXPENSE", new BigDecimal("100.00")
            );

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertEquals(new BigDecimal("-50.00"), result.getBreakdown().get("REFUND"));
        }

        @Test
        @DisplayName("Breakdown with very large values is allowed")
        void withBreakdown_withLargeValues_isAllowed() {
            Map<String, BigDecimal> breakdown = Map.of(
                "BIG", new BigDecimal("99999999999999.99")
            );

            AnalysisResult result = AnalysisResult.withBreakdown(validType, validValue, breakdown);

            assertEquals(new BigDecimal("99999999999999.99"), result.getBreakdown().get("BIG"));
        }

        @Test
        @DisplayName("Reference value with many decimal places is preserved")
        void withReference_withManyDecimals_isPreserved() {
            BigDecimal preciseValue = new BigDecimal("123.456789012345");

            AnalysisResult result = AnalysisResult.withReference(validType, validValue, preciseValue);

            assertEquals(preciseValue, result.getReferenceValue());
        }
    }
}

