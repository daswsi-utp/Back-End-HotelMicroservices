package com.microservice_promotions.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_room")
public class PromotionRoom {
    @EmbeddedId
    private PromotionRoomKey id = new PromotionRoomKey();
    @ManyToOne
    @MapsId("promotionId")
    @JoinColumn(name = "fk_promotion", nullable = false)
    private Promotion promotion;
}
