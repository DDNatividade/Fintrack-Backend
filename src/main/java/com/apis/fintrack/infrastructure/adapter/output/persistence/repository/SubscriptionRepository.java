package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionJPAEntity, Long> {

    Optional<SubscriptionJPAEntity> findByCostumer(UserJPAEntity costumer);

    Page<SubscriptionJPAEntity> findByIsActiveTrue(Pageable pageable);



}

