package com.microservice_promotions;

import com.microservice_promotions.dto.PromotionRequestDTO;
import com.microservice_promotions.dto.PromotionResponseDTO;
import com.microservice_promotions.entitites.Promotion;
import com.microservice_promotions.service.IPromotionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class MicroservicePromotionsApplicationTests {
	@Autowired
	private IPromotionService iPromotionService;

	@Test
	public void Promotion_Save_ApplicabilitySelected_ReturnSavedPromotion(){
		Set<Long> ids = new HashSet<>();
		ids.add(7L);
		ids.add(8L);
		PromotionRequestDTO promotion = PromotionRequestDTO.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.selected)
				.roomIds(ids)
				.build();
		Promotion responsePromotion = iPromotionService.save(promotion);
		PromotionResponseDTO savedPromotion = iPromotionService.findById(responsePromotion.getPromotionId());
		Assertions.assertThat(savedPromotion).isNotNull();
		System.out.println(savedPromotion);
		Assertions.assertThat(savedPromotion.getCreatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getUpdatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getPromotionId()).isGreaterThan(0);
		Assertions.assertThat(savedPromotion.getRooms()).isNotNull();
		Assertions.assertThat(savedPromotion.getRooms().size()).isEqualTo(2);
		iPromotionService.deletePromotion(responsePromotion.getPromotionId());
	}
	@Test
	public void Promotion_Save_ApplicabilityAll_ReturnSavedPromotion(){
		PromotionRequestDTO promotion = PromotionRequestDTO.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		Promotion responsePromotion = iPromotionService.save(promotion);
		PromotionResponseDTO savedPromotion = iPromotionService.findById(responsePromotion.getPromotionId());
		Assertions.assertThat(savedPromotion).isNotNull();
		System.out.println(savedPromotion);
		Assertions.assertThat(savedPromotion.getCreatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getUpdatedAt()).isNotNull();
		Assertions.assertThat(savedPromotion.getPromotionId()).isGreaterThan(0);
		Assertions.assertThat(savedPromotion.getRooms()).isNotNull();
		//This is a temporary assertion. In my DB there are only 3 existing rooms
		System.out.println(savedPromotion.getRooms());
		//Assertions.assertThat(savedPromotion.getRooms().size()).isEqualTo(3);
		iPromotionService.deletePromotion(responsePromotion.getPromotionId());
	}
	@Test
	public void Promotion_FindByID_ReturnFoundPromotion(){
		PromotionRequestDTO promotion = PromotionRequestDTO.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		Promotion responsePromotion = iPromotionService.save(promotion);
		PromotionResponseDTO foundPromotion = iPromotionService.findById(responsePromotion.getPromotionId());
		Assertions.assertThat(foundPromotion).isNotNull();
		iPromotionService.deletePromotion(foundPromotion.getPromotionId());
	}
	@Test
	public void Promotion_FindAll_ReturnPromotionList(){
		PromotionRequestDTO promotion1 = PromotionRequestDTO.builder().
				name("Test1")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		iPromotionService.save(promotion1);
		PromotionRequestDTO promotion2 = PromotionRequestDTO.builder().
				name("Test2")
				.description("Test description")
				.discountValue(null)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.added_value)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		iPromotionService.save(promotion2);
		List<PromotionResponseDTO> foundPromotionList = iPromotionService.findAll();
		Assertions.assertThat(foundPromotionList).isNotNull();
		iPromotionService.deletePromotion(foundPromotionList.getFirst().getPromotionId());
	}
	@Test
	public void Promotion_UpdatePromotion_SameApplicability_ReturnUpdatedPromotion(){
		PromotionRequestDTO promotion = PromotionRequestDTO.builder().
				name("Test")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		Promotion responsePromotion = iPromotionService.save(promotion);
		PromotionResponseDTO foundPromotion = iPromotionService.findById(responsePromotion.getPromotionId());
		Assertions.assertThat(foundPromotion).isNotNull();
		promotion.setName("Test_updated");
		promotion.setDiscountValue(null);
		promotion.setType(Promotion.Type.added_value);
		promotion.setIsActive(true);
		iPromotionService.update(foundPromotion.getPromotionId(), promotion);
		foundPromotion = iPromotionService.findById(responsePromotion.getPromotionId());
		Assertions.assertThat(foundPromotion).isNotNull();
		Assertions.assertThat(foundPromotion.getName()).isNotEqualTo("Test");
		Assertions.assertThat(foundPromotion.getDiscountValue()).isNull();
		Assertions.assertThat(foundPromotion.getUpdatedAt())
				.isNotEqualTo(foundPromotion.getCreatedAt());
		iPromotionService.deletePromotion(foundPromotion.getPromotionId());
	}
	@Test
	public void Promotion_FindByNameAndOrStatus_BothParametersProvided(){
		PromotionRequestDTO promotion1 = PromotionRequestDTO.builder().
				name("Test1")
				.description("Test description")
				.discountValue(20.0)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.percentage)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		iPromotionService.save(promotion1);
		PromotionRequestDTO promotion2 = PromotionRequestDTO.builder().
				name("Test2")
				.description("Test description")
				.discountValue(null)
				.startDate(Date.valueOf("2025-06-15"))
				.endDate(Date.valueOf( "2025-07-15"))
				.type(Promotion.Type.added_value)
				.roomApplicability(Promotion.RoomApplicability.all)
				.roomIds(null)
				.build();
		Promotion responsePromotion = iPromotionService.save(promotion2);
		//Both parameters provided
		List<PromotionResponseDTO> foundPromotions = iPromotionService.findByNameAndIsActive("Test", true);
		Assertions.assertThat(foundPromotions.size()).isEqualTo(2);
		promotion2.setIsActive(false);
		iPromotionService.update(responsePromotion.getPromotionId(), promotion2);
		foundPromotions = iPromotionService.findByNameAndIsActive("Test2", null);
		Assertions.assertThat(foundPromotions.size()).isEqualTo(1);
		iPromotionService.deletePromotion(responsePromotion.getPromotionId());
	}
	@Test
	void contextLoads() {
	}

}
