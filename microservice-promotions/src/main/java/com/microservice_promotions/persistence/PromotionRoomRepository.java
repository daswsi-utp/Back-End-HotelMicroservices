package com.microservice_promotions.persistence;

import com.microservice_promotions.entitites.PromotionRoom;
import com.microservice_promotions.entitites.PromotionRoomKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PromotionRoomRepository extends JpaRepository<PromotionRoom, PromotionRoomKey> {
    Set<PromotionRoom> findByPromotionIdPromotion(Long id);
}
