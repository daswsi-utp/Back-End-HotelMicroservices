package com.microservice_promotions.persistence;

import com.microservice_promotions.entitites.PromotionRoomType;
import com.microservice_promotions.entitites.PromotionRoomTypeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PromotionRoomTypeRepository extends JpaRepository<PromotionRoomType, PromotionRoomTypeKey> {
    Set<PromotionRoomType> findByPromotion_PromotionId(Long id);
}
