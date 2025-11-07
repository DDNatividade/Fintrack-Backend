package com.apis.fintrack.DAO;

import com.apis.fintrack.Entity.PropertyEntity;
import com.apis.fintrack.Entity.PropertyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    @Query(value = "SELECT p from PropertyEntity p WHERE p.property=?1")
    Optional<PropertyEntity> findByPropertyName(PropertyEnum property);
}
