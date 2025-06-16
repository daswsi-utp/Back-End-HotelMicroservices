package com.microservice_promotions;

import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.service.IPromotionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

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
		iPromotionService.save(promotion);
		Promotion savedPromotion = iPromotionService.findById(promotion.getPromotionId());
		Assertions.assertThat(savedPromotion).isNotNull();
		System.out.println(savedPromotion);
		Assertions.assertThat(savedPromotion.getCreatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getUpdatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getPromotionId()).isGreaterThan(0);
		iPromotionService.deletePromotion(promotion.getPromotionId());
	}
	@Test
	public void Promotion_FindByID_ReturnFoundPromotion(){
		Promotion promotion = Promotion.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.build();
		iPromotionService.save(promotion);
		Promotion foundPromotion = iPromotionService.findById(promotion.getPromotionId());
		Assertions.assertThat(foundPromotion).isNotNull();
		iPromotionService.deletePromotion(promotion.getPromotionId());
	}
	@Test
	public void Promotion_FindAll_ReturnPromotionList(){
		Promotion promotion1 = Promotion.builder().
				name("Test1")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.build();
		iPromotionService.save(promotion1);
		Promotion promotion2 = Promotion.builder().
				name("Test2")
				.description("Test description")
				.discountValue(null)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.added_value)
				.build();
		iPromotionService.save(promotion2);
		List<Promotion> foundPromotionList = iPromotionService.findAll();
		Assertions.assertThat(foundPromotionList).isNotNull();
		iPromotionService.deletePromotion(promotion1.getPromotionId());
	}
	@Test
	public void Promotion_UpdatePromotion_ReturnUpdatedPromotion(){
		Promotion promotion = Promotion.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.build();
		iPromotionService.save(promotion);
		Promotion foundPromotion = iPromotionService.findById(promotion.getPromotionId());
		Assertions.assertThat(foundPromotion).isNotNull();
		foundPromotion.setName("Test_updated");
		foundPromotion.setDiscountValue(null);
		foundPromotion.setType(Promotion.Type.added_value);
		iPromotionService.update(foundPromotion.getPromotionId(), foundPromotion);
		Assertions.assertThat(foundPromotion).isNotNull();
		Assertions.assertThat(foundPromotion.getName()).isNotEqualTo("Test");
		Assertions.assertThat(foundPromotion.getDiscountValue()).isNull();
		iPromotionService.deletePromotion(foundPromotion.getPromotionId());
	}
	@Test
	public void Promotion_FindByNameAndOrStatus_BothParametersProvided(){
		Promotion promotion1 = Promotion.builder().
				name("Test1")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.build();
		iPromotionService.save(promotion1);
		Promotion promotion2 = Promotion.builder().
				name("Test2")
				.description("Test description")
				.discountValue(null)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.added_value)
				.build();
		iPromotionService.save(promotion2);
		//Both parameters provided
		List<Promotion> foundPromotions = iPromotionService.findByNameAndIsActive("Test", true);
		Assertions.assertThat(foundPromotions.size()).isEqualTo(2);
		promotion2.setIsActive(false);
		iPromotionService.update(promotion2.getPromotionId(), promotion2);
		foundPromotions = iPromotionService.findByNameAndIsActive("Test2", null);
		Assertions.assertThat(foundPromotions.size()).isEqualTo(1);
		iPromotionService.deletePromotion(promotion2.getPromotionId());
	}
	@Test
	void contextLoads() {
	}

}
