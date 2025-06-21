package com.microservice_promotions.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomDTO {
    private Long roomId;
    private String roomType;
}
