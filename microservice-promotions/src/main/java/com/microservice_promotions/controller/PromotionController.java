package com.microservice_promotions.controller;

import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.service.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PromotionController {
    @Autowired
    private IPromotionService iPromotionService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Promotion savePromotion(@RequestBody PromotionRequestDTO promotion){
        return iPromotionService.save(promotion);
    }
    @GetMapping("/all")
    public ResponseEntity<?> findAllPromotions(){
        return ResponseEntity.ok(iPromotionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPromotionById(@PathVariable Long id){
        return ResponseEntity.ok(iPromotionService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable Long id, @RequestBody PromotionRequestDTO promotion){
        return ResponseEntity.ok(iPromotionService.update(id, promotion));
    }
    @GetMapping("/find")
    public ResponseEntity<?> findPromotionByNameAndStatus(@RequestParam(required = false) String name, @RequestParam(required = false) Boolean isActive){
        return ResponseEntity.ok(iPromotionService.findByNameAndIsActive(name, isActive));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findPromotionByName(@PathVariable String name){
        return ResponseEntity.ok(iPromotionService.getPromotionsByName(name));
    }
    @GetMapping("/status/{isActive}")
    public ResponseEntity<?> findPromotionByStatus(@PathVariable boolean isActive){
        return ResponseEntity.ok(iPromotionService.getPromotionsByIsActive(isActive));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePromotionById(@PathVariable Long id){
        return ResponseEntity.ok(iPromotionService.deletePromotion(id));
    }
}
