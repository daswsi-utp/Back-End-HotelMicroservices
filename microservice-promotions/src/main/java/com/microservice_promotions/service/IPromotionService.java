package com.microservice_promotions.service;

import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.entitites.Promotion;

import java.util.List;
public interface IPromotionService {
    List<PromotionResponseDTO> findAll();
    Promotion findById(Long id);
    Promotion save(Promotion promotion);
    Promotion update(Long id, Promotion promotion);
    List<PromotionResponseDTO> getPromotionsByName(String name);
    List<PromotionResponseDTO> getPromotionsByIsActive(boolean isActive);
    boolean deletePromotion(Long id);
    List<PromotionResponseDTO> findByNameAndIsActive(String name, Boolean isActive);
}
