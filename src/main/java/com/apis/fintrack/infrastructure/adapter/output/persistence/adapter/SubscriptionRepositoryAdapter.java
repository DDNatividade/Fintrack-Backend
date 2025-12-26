package com.apis.fintrack.infrastructure.adapter.output.persistence.adapter;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.SubscriptionPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.SubscriptionRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto de salida SubscriptionRepositoryPort.
 *
 * Traduce las operaciones del dominio a llamadas a Spring Data JPA y convierte
 * entre SubscriptionJPAEntity y Subscription (dominio) usando SubscriptionPersistenceMapper.
 */
@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionPersistenceMapper mapper;

    public SubscriptionRepositoryAdapter(SubscriptionRepository subscriptionRepository,
                                         UserRepository userRepository,
                                         SubscriptionPersistenceMapper mapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Subscription> findByCustomer(User customer) {
        if (customer == null || customer.getId() == null || customer.getId().isEmpty()) {
            return Optional.empty();
        }
        Long userId = customer.getId().getValue();
        Optional<UserJPAEntity> userEntityOpt = userRepository.searchById(userId);
        if (userEntityOpt.isEmpty()) {
            return Optional.empty();
        }
        Optional<SubscriptionJPAEntity> jpaOpt = subscriptionRepository.findByCostumer(userEntityOpt.get());
        return jpaOpt.map(mapper::toDomain);
    }

    @Override
    public Optional<Subscription> findById(Long subscriptionId) {
        Objects.requireNonNull(subscriptionId, "Subscription id must not be null");
        Optional<SubscriptionJPAEntity> jpaOpt = subscriptionRepository.findById(subscriptionId);
        return jpaOpt.map(mapper::toDomain);
    }

    @Override
    public Page<Subscription> findActiveSubscriptions(Pageable page) {
        Objects.requireNonNull(page, "Pageable must not be null");
        Page<SubscriptionJPAEntity> jpaPage = subscriptionRepository.findByIsActiveTrue(page);
        return jpaPage.map(mapper::toDomain);
    }

    @Override
    public long countActiveSubscriptions() {
        // Prefer repository.countByIsActiveTrue() if available for efficiency
        Page<SubscriptionJPAEntity> jpaPage = subscriptionRepository.findByIsActiveTrue(Pageable.unpaged());
        return jpaPage.getTotalElements();
    }

    @Override
    public Subscription save(Subscription subscription) {
        Objects.requireNonNull(subscription, "Subscription must not be null");
        SubscriptionJPAEntity entity = mapper.toJpaEntity(subscription);
        SubscriptionJPAEntity saved = subscriptionRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Page<Subscription> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "Pageable must not be null");
        Page<SubscriptionJPAEntity> jpaPage = subscriptionRepository.findAll(pageable);
        return jpaPage.map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long subscriptionId) {
        Objects.requireNonNull(subscriptionId, "Subscription id must not be null");
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        Objects.requireNonNull(userId, "User id must not be null");
        Optional<UserJPAEntity> userEntityOpt = userRepository.findById(userId);
        if (userEntityOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id " + userId + " does not exist");
        }
        // If a subscription exists for this user, delete it
        Optional<SubscriptionJPAEntity> subscriptionOpt = subscriptionRepository.findByCostumer(userEntityOpt.get());
        subscriptionOpt.ifPresent(subscriptionRepository::delete);
    }

    @Override
    public boolean existsById(Long subscriptionId) {
        Objects.requireNonNull(subscriptionId, "Subscription id must not be null");
        return subscriptionRepository.existsById(subscriptionId);
    }

}
