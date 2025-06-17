package com.microservice_promotions.service;

import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.persistence.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImp implements IPromotionService{
    @Autowired
    private PromotionRepository promotionRepository;
    @Override
    public List<PromotionResponseDTO> findAll() {
        List<Promotion> promotionList = promotionRepository.findAll();
        List<PromotionResponseDTO> responseList = new ArrayList<>();
        for(Promotion promotion : promotionList){

        }
        return responseList;
    }
    @Override
    public Promotion findById(Long id) {
        return promotionRepository.findById(id).orElseThrow();
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
        return promotionRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<PromotionResponseDTO> getPromotionsByIsActive(boolean isActive) {
        return promotionRepository.findByIsActive(isActive);
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
        return promotionRepository.findByNameAndStatus(name, isActive);
    }
}
