package com.microservice_promotions.dto;

import com.microservice_promotions.entitites.Promotion;
import lombok.*;

import java.sql.Date;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRequestDTO {
    String name;
    String description;
    Double discountValue;
    Date startDate;
    Date endDate;
    Promotion.Type type;
    Boolean isActive;
    int minStay;
    Promotion.RoomApplicability roomApplicability;
    Set<Long> roomIds;
}
