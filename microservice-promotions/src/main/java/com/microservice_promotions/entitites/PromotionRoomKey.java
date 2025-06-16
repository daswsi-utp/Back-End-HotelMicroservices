package com.microservice_promotions.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRoomKey {
    @Column(name = "fk_promotion")
    private Long promotionId;
    @Column(name = "fk_room")
    private Long roomId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof PromotionRoomKey that)) return false;
        return Objects.equals(promotionId, that.promotionId) && Objects.equals(roomId, that.roomId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(promotionId, roomId);
    }
}
