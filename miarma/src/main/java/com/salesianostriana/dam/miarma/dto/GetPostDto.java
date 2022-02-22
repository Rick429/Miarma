package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.model.Tipo;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetPostDto {

    private Long id;
    private String nick;
    private String titulo;
    private String descripcion;
    private String archivo;
    private String archivoreescalado;
    private Tipo tipopublicacion;
    private List<GetCommentDto> commentarios;
    private int numlikes;
    private UUID userid;

}
