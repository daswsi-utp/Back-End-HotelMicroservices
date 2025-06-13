package com.microservice_promotions.service;

import com.microservice_promotions.entitites.Promotion;

import java.util.List;
public interface IPromotionService {
    List<Promotion> findAll();
    Promotion findById(Long id);
    Promotion save(Promotion promotion);
    Promotion update(Long id, Promotion promotion);
    List<Promotion> getPromotionsByName(String name);
    List<Promotion> getPromotionsByIsActive(boolean isActive);
}
