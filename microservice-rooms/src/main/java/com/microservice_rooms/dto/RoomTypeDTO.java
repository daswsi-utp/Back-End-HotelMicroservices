package com.microservice_rooms.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomTypeDTO {
    Long id;
    String name;
}
