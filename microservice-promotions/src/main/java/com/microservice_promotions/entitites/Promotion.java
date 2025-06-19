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
    //Be wary, now that is it an object Boolean and not a primitive boolean
    //There might be edge cases where when a promotion gets updated, the is_active field gets set to null
    //This is improbable to happen, but we have to be caoutis
    Boolean isActive;
    @Column(name = "minimun_stay")
    int minStay;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_applicability")
    RoomApplicability roomApplicability;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PromotionRoom> promotionRoomList;
    //This is the code for a trigger function to replace a "null" is_active field when a row is updated
    //This edgecase hasn't happened yet, but I'm paranoic so I'll write it anyways
    //This function doesn't exist YET in the DB but if this edgecase happens tell me
    //So I can add it
    /*
   CREATE OR REPLACE FUNCTION set_is_active_default()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.is_active IS NULL THEN
    NEW.is_active := TRUE;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_is_active_on_update
BEFORE UPDATE ON promotions
FOR EACH ROW
EXECUTE FUNCTION set_is_active_default();

     */
}
