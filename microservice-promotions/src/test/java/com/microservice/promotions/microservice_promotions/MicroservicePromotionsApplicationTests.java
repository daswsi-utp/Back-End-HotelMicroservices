package com.microservice.promotions.microservice_promotions;

import com.microservice.promotions.microservice_promotions.entities.Promotion;
import com.microservice.promotions.microservice_promotions.service.IPromotionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
class MicroservicePromotionsApplicationTests {
	@Autowired
	private IPromotionService iPromotionService;

	@Test
	public void Promotion_Save_ReturnSavedPromotion(){
		Promotion promotion = Promotion.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.build();
		Promotion savedPromotion = iPromotionService.save(promotion);
		Assertions.assertThat(savedPromotion).isNotNull();
		Assertions.assertThat(savedPromotion.getPromotionId()).isGreaterThan(0);
	}
	@Test
	void contextLoads() {
	}

}
