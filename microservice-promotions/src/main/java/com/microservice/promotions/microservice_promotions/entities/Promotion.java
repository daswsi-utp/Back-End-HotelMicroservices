package com.microservice.promotions.microservice_promotions.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Builder
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    Long promotionId;
    @Column(name = "promotion_name")
    String name;
    @Column(name = "promotion_description")
    String description;
    @Column(name = "discount_value")
    double discountValue;
    @Column(name = "start_date")
    Date startDate;
    @Column(name = "end_date")
    Date endDate;
    @Column(name = "created_at")
    Timestamp createdAt;
    @Column(name = "updated_at")
    Timestamp updatedAt;
    String type;
}
