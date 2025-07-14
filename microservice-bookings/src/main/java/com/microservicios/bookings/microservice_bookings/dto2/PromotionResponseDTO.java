package com.microservicios.bookings.microservice_bookings.dto2;

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
    String type;
    Boolean isActive;
    int minStay;
    String roomApplicability;
}
