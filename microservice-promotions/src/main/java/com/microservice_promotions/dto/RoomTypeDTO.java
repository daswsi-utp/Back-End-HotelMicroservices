package com.microservice_promotions.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomTypeDTO {
    private Long id;
    private String name;
}
