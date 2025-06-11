package com.microservice.promotions.microservice_promotions.persistence;

import com.microservice.promotions.microservice_promotions.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
