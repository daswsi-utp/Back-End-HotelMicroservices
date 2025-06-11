package com.microservice.promotions.microservice_promotions.controller;

import com.microservice.promotions.microservice_promotions.entities.Promotion;
import com.microservice.promotions.microservice_promotions.service.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    @Autowired
    private IPromotionService iPromotionService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Promotion savePromotion(@RequestBody Promotion promotion){
        return iPromotionService.save(promotion);
    }
    @GetMapping("/all")
    public ResponseEntity<?> findAllPromotions(){
        return ResponseEntity.ok(iPromotionService.findAll());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findPromotionById(@PathVariable Long id){
        return ResponseEntity.ok(iPromotionService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable Long id, Promotion promotion){
        return ResponseEntity.ok(iPromotionService.update(id, promotion));
    }
}
