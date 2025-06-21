package com.microservice_rooms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomTypeDTO {
    private Long roomTypeId;
    private String roomType;
}
