package com.salesianostriana.dam.miarma.dto;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetCommentDto {

    private String comentario;
    private UUID user_id;
    private Long post_id;
    private String nick;
    private String image;

}
