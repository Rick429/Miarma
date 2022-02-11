package com.salesianostriana.dam.miarma.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Post implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String titulo;

    private String descripcion;

    private String archivo;


}
