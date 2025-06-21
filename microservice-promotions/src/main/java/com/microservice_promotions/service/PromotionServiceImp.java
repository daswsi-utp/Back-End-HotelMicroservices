package com.microservice_promotions.service;

import com.microservice_promotions.client.RoomClient;
import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.dto.RoomDTO;
import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.entitites.PromotionRoom;
import com.microservice_promotions.entitites.PromotionRoomKey;
import com.microservice_promotions.persistence.PromotionRepository;
import com.microservice_promotions.persistence.PromotionRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        Set<RoomDTO> roomList = getRoomType(id);
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
    public PromotionResponseDTO save(PromotionRequestDTO promotionRequest) {
        Promotion promotion = new Promotion();
        promotion.setName(promotionRequest.getName());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setDiscountValue(promotionRequest.getDiscountValue());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setType(promotionRequest.getType());
        promotion.setIsActive(promotionRequest.getIsActive());
        promotion.setRoomApplicability(promotionRequest.getRoomApplicability());
        promotion.setMinStay(promotionRequest.getMinStay());
        Promotion savedPromotion =promotionRepository.save(promotion);
        if(promotionRequest.getRoomIds() != null){
            for(Long roomId : promotionRequest.getRoomIds()){
                PromotionRoom promotionRoom =  new PromotionRoom();
                PromotionRoomKey key = new PromotionRoomKey();
                key.setPromotionId(promotion.getPromotionId());
                key.setRoomId(roomId);
                promotionRoom.setId(key);
                promotionRoom.setPromotion(savedPromotion);
                promotionRoomRepository.save(promotionRoom);
            }
        }
        return findById(savedPromotion.getPromotionId());
    }

    @Override
    @Transactional
    public PromotionResponseDTO update(Long id, PromotionRequestDTO promotion) {
        Optional<Promotion> existingPromotion = promotionRepository.findById(id);
        if(existingPromotion.isEmpty()){
            return null;
        }
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
        promotionRepository.save(updatePromotion);
        PromotionResponseDTO updatedPromotion = findById(updatePromotion.getPromotionId());
        //Promotion updatedPromotion = promotionRepository.findById(updatePromotion.getPromotionId());

        if(promotion.getRoomApplicability().equals(Promotion.RoomApplicability.selected)){
            Set<Long> newRoomIds =  promotion.getRoomIds() != null ? promotion.getRoomIds() : new HashSet<>();
            Set<Long> currentRoomIds = promotionRoomRepository.findByPromotion_PromotionId(id).stream().map(pr -> pr.getId().getRoomId()).collect(Collectors.toSet());
            for(Long roomId: currentRoomIds){
                if(!currentRoomIds.contains(roomId)){
                    promotionRoomRepository.deleteById(new PromotionRoomKey(id, roomId));
                }
            }
            for(Long roomId: newRoomIds){
                if(!currentRoomIds.contains(roomId)){
                    PromotionRoom pr = new PromotionRoom();
                    pr.setId(new PromotionRoomKey(id, roomId));
                    pr.setPromotion(updatePromotion);
                    promotionRoomRepository.save(pr);
                }
            }
        }
        return updatedPromotion;

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
    public Set<RoomDTO> getRoomType(Long id){
        Set<PromotionRoom> promotionRooms = promotionRoomRepository.findByPromotion_PromotionId(id);
        Set<RoomDTO> roomDTOS = new HashSet<>();
        for(PromotionRoom pr : promotionRooms){
            Long promotionId = pr.getId().getPromotionId();
            RoomDTO roomDTO = roomClient.getRoomById(pr.getId().getRoomId());
            roomDTOS.add(roomDTO);
        }
        return roomDTOS;
    }
    @Override
    public List<PromotionResponseDTO> createPromotionResponseList(List<Promotion> promotions){
        List<PromotionResponseDTO> responseList = new ArrayList<>();
        for(Promotion promotion : promotions){
            Set<RoomDTO> roomList = getRoomType(promotion.getPromotionId());
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
