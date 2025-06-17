package com.microservice_promotions.dto;

import com.microservice_promotions.entitites.Promotion;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResponseDTO {
    Long promotionId;
    String name;
    String description;
    Double discountValue;
    Date startDate;
    Date endDate;
    Timestamp createdAt;
    Timestamp updatedAt;
    Promotion.Type type;
    Boolean isActive;
    int minStay;
    Promotion.RoomApplicability roomApplicability;
    Set<RoomDTO> rooms;
}
