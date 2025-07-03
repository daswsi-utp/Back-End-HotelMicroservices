package com.microservice_promotions.persistence;

import com.microservice_promotions.entitites.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PromotionRepository extends  JpaRepository<Promotion, Long>{
    List<Promotion> findByNameContainingIgnoreCase(String name);
    List<Promotion> findByIsActive(boolean isActive);
    @Query("SELECT p FROM Promotion p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (:isActive IS NULL OR p.isActive = :isActive)")
    List<Promotion> findByNameAndStatus(@Param("name") String name,@Param("isActive") Boolean isActive);
}
