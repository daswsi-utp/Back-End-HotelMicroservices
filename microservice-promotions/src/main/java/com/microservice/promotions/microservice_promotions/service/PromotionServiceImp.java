package com.microservice.promotions.microservice_promotions.service;

import com.microservice.promotions.microservice_promotions.entities.Promotion;
import com.microservice.promotions.microservice_promotions.persistence.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImp implements  IPromotionService{
    @Autowired
    private PromotionRepository promotionRepository;
    @Override
    public List<Promotion> findAll() {
        return promotionRepository.findAll();
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
            updatePromotion.setActive(promotion.isActive());
            return promotionRepository.save(promotion);
        }
        return null;
    }
}
