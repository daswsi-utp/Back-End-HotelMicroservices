package com.microservice_promotions.service;

import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.dto.RoomTypeDTO;
import com.microservice_promotions.entitites.Promotion;

import java.util.List;
import java.util.Set;

public interface IPromotionService {
    List<PromotionResponseDTO> findAll();
    PromotionResponseDTO findById(Long id);
    PromotionResponseDTO save(PromotionRequestDTO promotionRequest);
    PromotionResponseDTO update(Long id, PromotionRequestDTO promotion);
    List<PromotionResponseDTO> getPromotionByRoomTypeId(Long id);
    List<PromotionResponseDTO> getPromotionsByName(String name);
    List<PromotionResponseDTO> getPromotionsByIsActive(boolean isActive);
    boolean deletePromotion(Long id);
    List<PromotionResponseDTO> findByNameAndIsActive(String name, Boolean isActive);
    Set<RoomTypeDTO> getRoomType(Long id);
    List<PromotionResponseDTO> createPromotionResponseList(List<Promotion> promotions);
}
