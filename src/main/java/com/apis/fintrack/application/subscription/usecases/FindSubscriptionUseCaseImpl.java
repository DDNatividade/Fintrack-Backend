package com.apis.fintrack.application.subscription.usecases;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.input.FindSubscriptionUseCase;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import com.apis.fintrack.domain.user.model.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindSubscriptionUseCaseImpl implements FindSubscriptionUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;

    public FindSubscriptionUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> findById(Long subscriptionId) {
        // El puerto de repositorio no provee findById por id primario; usaremos findAll y filtrar
        var page = subscriptionRepository.findAll(org.springframework.data.domain.PageRequest.of(0, 50));
        return page.stream().filter(s -> s.getId() != null && s.getId().getValue().equals(subscriptionId)).findFirst();
    }

    @Override
    public Optional<Subscription> findByUserId(UserId userId) {

        return subscriptionRepository.findById(userId.getValue());
    }
}

