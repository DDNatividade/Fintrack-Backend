package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PropertyJPAEntity;
import com.apis.fintrack.infrastructure.security.model.PropertyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyJPAEntity, Long> {
    @Query(value = "SELECT p from PropertyJPAEntity p WHERE p.property=?1")
    Optional<PropertyJPAEntity> findByPropertyName(PropertyEnum property);
}

