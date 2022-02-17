package com.salesianostriana.dam.miarma.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetLikeDto {

    private Long id;
    private UUID user_id;
    private Long post_id;

}
