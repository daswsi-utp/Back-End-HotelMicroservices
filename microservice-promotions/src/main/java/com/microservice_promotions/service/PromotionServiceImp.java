package com.microservice_promotions.service;

import com.microservice_promotions.client.RoomClient;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.dto.RoomDTO;
import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.entitites.PromotionRoom;
import com.microservice_promotions.persistence.PromotionRepository;
import com.microservice_promotions.persistence.PromotionRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromotionServiceImp implements IPromotionService{
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private RoomClient roomClient;
    @Autowired
    private PromotionRoomRepository promotionRoomRepository;
    @Override
    public List<PromotionResponseDTO> findAll() {
        List<Promotion> promotionList = promotionRepository.findAll();
        return createPromotionResponseList(promotionList);
    }
    @Override
    public PromotionResponseDTO findById(Long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow();
        Set<RoomDTO> roomList = getRoomName(id);
        return PromotionResponseDTO.builder()
                .promotionId(promotion.getPromotionId())
                .name(promotion.getName())
                .description(promotion.getDescription())
                .discountValue(promotion.getDiscountValue())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .type(promotion.getType())
                .isActive(promotion.getIsActive())
                .minStay(promotion.getMinStay())
                .roomApplicability(promotion.getRoomApplicability())
                .rooms(roomList)
                .build();
    }

    @Override
    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion update(Long id, Promotion promotion) {
        Optional<Promotion> existingPromotion = promotionRepository.findById(id);
        if(existingPromotion.isPresent()){
            Promotion updatePromotion = existingPromotion.get();
            updatePromotion.setName(promotion.getName());
            updatePromotion.setDescription(promotion.getDescription());
            updatePromotion.setDiscountValue(promotion.getDiscountValue());
            updatePromotion.setStartDate(promotion.getStartDate());
            updatePromotion.setEndDate(promotion.getEndDate());
            updatePromotion.setType(promotion.getType());
            updatePromotion.setIsActive(promotion.getIsActive());
            updatePromotion.setMinStay(promotion.getMinStay());
            updatePromotion.setRoomApplicability(promotion.getRoomApplicability());
            return promotionRepository.save(updatePromotion);
        }
        return null;
    }

    @Override
    public List<PromotionResponseDTO> getPromotionsByName(String name) {
        List<Promotion> matchingPromotions = promotionRepository.findByNameContainingIgnoreCase(name);
        return createPromotionResponseList(matchingPromotions);
    }

    @Override
    public List<PromotionResponseDTO> getPromotionsByIsActive(boolean isActive) {
        List<Promotion> matchingPromotions = promotionRepository.findByIsActive(isActive);
        return createPromotionResponseList(matchingPromotions);
    }
    @Override
    public boolean deletePromotion(Long id){
        if(promotionRepository.existsById(id)){
            promotionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<PromotionResponseDTO> findByNameAndIsActive(String name, Boolean isActive) {
        List<Promotion> matchingPromotions = promotionRepository.findByNameAndStatus(name, isActive);
        return createPromotionResponseList(matchingPromotions);
    }
    @Override
    public Set<RoomDTO> getRoomName(Long id){
        Set<PromotionRoom> promotionRooms = promotionRoomRepository.findByPromotionIdPromotion(id);
        Set<RoomDTO> roomDTOS = new HashSet<>();
        for(PromotionRoom pr : promotionRooms){
            Long promotionId = pr.getId().getPromotionId();
            RoomDTO roomDTO = roomClient.getRoomById(promotionId);
            roomDTOS.add(roomDTO);
        }
        return roomDTOS;
    }
    @Override
    public List<PromotionResponseDTO> createPromotionResponseList(List<Promotion> promotions){
        List<PromotionResponseDTO> responseList = new ArrayList<>();
        for(Promotion promotion : promotions){
            Set<RoomDTO> roomList = getRoomName(promotion.getPromotionId());
            PromotionResponseDTO responseDTO = PromotionResponseDTO.builder()
                    .promotionId(promotion.getPromotionId())
                    .name(promotion.getName())
                    .description(promotion.getDescription())
                    .discountValue(promotion.getDiscountValue())
                    .startDate(promotion.getStartDate())
                    .endDate(promotion.getEndDate())
                    .createdAt(promotion.getCreatedAt())
                    .updatedAt(promotion.getUpdatedAt())
                    .type(promotion.getType())
                    .isActive(promotion.getIsActive())
                    .minStay(promotion.getMinStay())
                    .roomApplicability(promotion.getRoomApplicability())
                    .rooms(roomList)
                    .build();
            responseList.add(responseDTO);
        }
        return responseList;
    }
}
