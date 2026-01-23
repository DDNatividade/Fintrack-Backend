package com.apis.fintrack.domain.transaction.model;
import java.util.Objects;

/**
 * Value Object representing a transaction category.
 *
 * Business Rules:
 * - A transaction can only have ONE category
 * - Category is mandatory
 *
 * Characteristics:
 * - Immutable
 * - Wraps the enum TransactionCategoryEnum
 */
public final class Category {
    
    private final TransactionCategoryEnum value;
    
    private Category(TransactionCategoryEnum value) {
        this.value = value;
    }
    
    /**
     * Creates a Category from a TransactionCategoryEnum.
     *
     * @param category the category enum
     * @return a new Category instance
     * @throws IllegalArgumentException if category is null
     */
    public static Category of(TransactionCategoryEnum category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return new Category(category);
    }
    
    /**
     * Creates a Category from the enum name.
     *
     * @param categoryName the category name
     * @return a new Category instance
     * @throws IllegalArgumentException if name is invalid
     */
    public static Category fromString(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        try {
            TransactionCategoryEnum category = TransactionCategoryEnum.valueOf(categoryName.trim().toUpperCase());
            return new Category(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid category: " + categoryName
            );
        }
    }
    
    public TransactionCategoryEnum getValue() {
        return value;
    }
    
    /**
     * Checks if this is a housing category.
     */
    public boolean isHousing() {
        return value == TransactionCategoryEnum.HOUSING;
    }
    
    /**
     * Checks if this is a recreational category.
     */
    public boolean isRecreational() {
        return value == TransactionCategoryEnum.RECREATIONAL;
    }
    
    /**
     * Checks if this is an alimentation category.
     */
    public boolean isAlimentation() {
        return value == TransactionCategoryEnum.ALIMENTATION;
    }
    
    /**
     * Checks if this is an investment category.
     */
    public boolean isInversion() {
        return value == TransactionCategoryEnum.INVERSION;
    }

    /**
     * Checks if this is a transportation category.
     */
    public boolean isTransportation() {
        return value == TransactionCategoryEnum.TRANSPORTATION;
    }

    /**
     * Checks if this is a health category.
     */
    public boolean isHealth() {
        return value == TransactionCategoryEnum.HEALTH;
    }

    /**
     * Checks if this is an education category.
     */
    public boolean isEducation() {
        return value == TransactionCategoryEnum.EDUCATION;
    }

    /**
     * Checks if this is a utilities category.
     */
    public boolean isUtilities() {
        return value == TransactionCategoryEnum.UTILITIES;
    }

    /**
     * Checks if this is a salary category.
     */
    public boolean isSalary() {
        return value == TransactionCategoryEnum.SALARY;
    }

    /**
     * Checks if this is a freelance category.
     */
    public boolean isFreelance() {
        return value == TransactionCategoryEnum.FREELANCE;
    }

    /**
     * Checks if this is a gifts category.
     */
    public boolean isGifts() {
        return value == TransactionCategoryEnum.GIFTS;
    }

    /**
     * Checks if this is a sales category.
     */
    public boolean isSales() {
        return value == TransactionCategoryEnum.SALES;
    }

    /**
     * Checks if this is the "other" category.
     */
    public boolean isOther() {
        return value == TransactionCategoryEnum.OTHER;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return value == category.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.name();
    }
}


