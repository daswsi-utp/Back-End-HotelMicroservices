package com.microservice_promotions.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {
    public enum Type{
        percentage, fixed, added_value
    }
    public enum RoomApplicability{
        all, selected
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    Long promotionId;
    @Column(name = "promotion_name")
    String name;
    @Column(name = "promotion_description")
    String description;
    @Column(name = "discount_value", nullable = true)
    Double discountValue;
    @Column(name = "start_date")
    Date startDate;
    @Column(name = "end_date")
    Date endDate;
    @Column(name = "created_at", updatable = false, insertable = false)
    Timestamp createdAt;
    @Column(name = "updated_at", updatable = false, insertable = false)
    Timestamp updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "promotion_type")
    Type type;
    @Column(name = "is_active", insertable = false)
    Boolean isActive;
    @Column(name = "minimun_stay")
    int minStay;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_applicability")
    RoomApplicability roomApplicability;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PromotionRoomType> promotionRoomTypeList;
}
