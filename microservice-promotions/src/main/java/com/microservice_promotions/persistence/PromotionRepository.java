package com.microservice_promotions.persistence;

import com.microservice_promotions.entitites.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PromotionRepository extends  JpaRepository<Promotion, Long>{
    List<Promotion> findByNameContainingIgnoreCase(String name);
    List<Promotion> findByIsActive(boolean isActive);
}
