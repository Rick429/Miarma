package com.salesianostriana.dam.miarma.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateCommentDto {

    @NotBlank
    private String comentario;

}
