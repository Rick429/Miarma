package com.salesianostriana.dam.miarma.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class CreatePostDto {
    
    private String titulo;
    private String descripcion;
    private String archivo;
}
