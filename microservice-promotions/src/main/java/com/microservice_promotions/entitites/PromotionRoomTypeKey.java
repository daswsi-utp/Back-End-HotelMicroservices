package com.microservice_promotions.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRoomTypeKey implements Serializable {
    @Column(name = "fk_promotion")
    private Long promotionId;
    @Column(name = "fk_roomType")
    private Long roomTypeId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof PromotionRoomTypeKey that)) return false;
        return Objects.equals(promotionId, that.promotionId) && Objects.equals(roomTypeId, that.roomTypeId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(promotionId, roomTypeId);
    }
}
