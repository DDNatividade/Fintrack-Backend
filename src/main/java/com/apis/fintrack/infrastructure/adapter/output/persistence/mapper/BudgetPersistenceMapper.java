package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;


import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.user.model.UserId;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.BudgetJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for converting between Budget (domain) and BudgetJPAEntity (JPA).
 *
 * Responsibilities:
 * - Convert JPA entities to domain entities
 * - Convert domain entities to JPA entities
 * - Update existing JPA entities with domain data
 */
@Component
public class BudgetPersistenceMapper {

    private final UserRepository userRepository;

    public BudgetPersistenceMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Converts a JPA entity to a domain entity.
     *
     * @param jpaEntity the JPA entity
     * @return the domain entity
     */
    public Budget toDomain(BudgetJPAEntity jpaEntity) {
        Objects.requireNonNull(jpaEntity, "jpaEntity must not be null");

        Budget budget = new Budget(
            new BudgetID(jpaEntity.getBudgetId()),
            UserId.of(jpaEntity.getUser().getUserId()),
            Money.of(jpaEntity.getLimitAmount()),
            jpaEntity.getCategory()
        );
        budget.changeCategory(jpaEntity.getCategory());

        return budget;
    }

    /**
     * Converts a domain entity to a JPA entity.
     *
     * @param domainBudget the domain entity
     * @return the JPA entity
     */
    public BudgetJPAEntity toJpaEntity(Budget domainBudget) {
        if (domainBudget == null) {
            return null;
        }

        BudgetJPAEntity jpaEntity = new BudgetJPAEntity();

        // Only assign ID if it exists (not new)
        if (!domainBudget.getBudgetID().isEmpty()) {
            jpaEntity.setBudgetId(domainBudget.getBudgetID().value());
        }

        jpaEntity.setLimitAmount(domainBudget.getLimit().getAmount());
        jpaEntity.setCategory(domainBudget.getCategory());
        jpaEntity.setStartDate(domainBudget.getPeriod().getStartDate());
        jpaEntity.setEndDate(domainBudget.getPeriod().getEndDate());

        // Find the user
        UserJPAEntity userEntity = userRepository.searchById(domainBudget.getUserId().getValue())
            .orElseThrow(() -> new IllegalArgumentException(
                "User does not exist with ID: " + domainBudget.getUserId().getValue()
            ));
        jpaEntity.setUser(userEntity);

        return jpaEntity;
    }

    /**
     * Updates an existing JPA entity with domain data.
     *
     * @param domainBudget the domain entity with new data
     * @param jpaEntity the JPA entity to update
     */
    public void updateJpaEntity(Budget domainBudget, BudgetJPAEntity jpaEntity) {
        if (domainBudget == null || jpaEntity == null) {
            return;
        }

        jpaEntity.setLimitAmount(domainBudget.getLimit().getAmount());
        jpaEntity.setCategory(domainBudget.getCategory());
        jpaEntity.setStartDate(domainBudget.getPeriod().getStartDate());
        jpaEntity.setEndDate(domainBudget.getPeriod().getEndDate());
    }
}

