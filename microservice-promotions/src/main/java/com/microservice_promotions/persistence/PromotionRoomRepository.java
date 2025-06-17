package com.microservice_promotions.persistence;

import com.microservice_promotions.entitites.PromotionRoom;
import com.microservice_promotions.entitites.PromotionRoomKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRoomRepository extends JpaRepository<PromotionRoom, PromotionRoomKey> {
    List<PromotionRoom> findByPromotionIdPromotion(Long id);
}
