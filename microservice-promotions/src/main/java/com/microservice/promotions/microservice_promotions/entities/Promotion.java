package com.microservice.promotions.microservice_promotions.entities;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Promotion {
    public enum Type{
        percentage, fixed, added_value
    }
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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "promotion_type")
    Type type;
    //In the DB a status of 1 indicates an active promotion, a status of 0 indicates an inactive one
    @Column(name = "is_active")
    boolean isActive;
}

