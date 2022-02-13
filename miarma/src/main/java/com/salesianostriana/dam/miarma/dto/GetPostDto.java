package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Tipo;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetPostDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String archivo;
    private Tipo tipopublicacion;
    private UUID userid;

}
