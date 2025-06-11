package com.microservice.promotions.microservice_promotions.persistence;

import com.microservice.promotions.microservice_promotions.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
