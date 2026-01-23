package com.apis.fintrack.domain.transaction.model;

import lombok.Getter;

/**
 * Enum representing transaction categories for financial tracking.
 * Each category includes a description for user reference.
 */
@Getter
public enum TransactionCategoryEnum {

    HOUSING("Home-related expenses"),
    RECREATIONAL("Money spent for entertainment purposes"),
    ALIMENTATION("Food-related expenses"),
    INVERSION("Expenses related to investments"),
    TRANSPORTATION("Expenses related to transport and travel"),
    HEALTH("Health and medical-related expenses"),
    EDUCATION("Expenses related to learning and education"),
    UTILITIES("Expenses for essential services like water, electricity, gas, internet, etc."),
    SALARY("Income from work or services rendered"),
    FREELANCE("Income from freelance work or independent projects"),
    GIFTS("Money received as gifts or donations"),
    SALES("Income from selling goods or services"),
    OTHER("Miscellaneous expenses or income that do not fit into other categories");

    /**
     * -- GETTER --
     *  Gets the description of this category.
     *
     */
    private final String description;

    TransactionCategoryEnum(String description) {
        this.description = description;
    }

    /**
     * Validates if a category is valid (not null).
     *
     * @param category the category to validate
     * @return true if category is valid
     */
    public static boolean isValid(TransactionCategoryEnum category) {
        return category != null;
    }

    /**
     * Converts a string to TransactionCategoryEnum.
     *
     * @param name the category name
     * @return the corresponding category
     * @throws IllegalArgumentException if category does not exist
     */
    public static TransactionCategoryEnum fromString(String name) {
        try {
            return TransactionCategoryEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + name);
        }
    }
}

