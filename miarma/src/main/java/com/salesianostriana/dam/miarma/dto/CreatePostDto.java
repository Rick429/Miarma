package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Tipo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class CreatePostDto {

    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @NotNull
    private Tipo tipopublicacion;
}
