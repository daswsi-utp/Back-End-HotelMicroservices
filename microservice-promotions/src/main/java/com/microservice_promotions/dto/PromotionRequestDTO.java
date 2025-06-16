package com.microservice_promotions.dto;

import com.microservice_promotions.entitites.Promotion;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

public class PromotionRequestDTO {
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
    Set<Long> roomIds;
}
