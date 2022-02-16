package com.salesianostriana.dam.miarma.model;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private String archivoreescalado;

    private Tipo tipopublicacion;

    @ManyToOne
    private UserEntity usuario;

    @Builder.Default
    @OneToMany(mappedBy = "postcomentado")
    private List<Comment> comentarios = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "postlikeado")
    private List<Megusta> likes = new ArrayList<>();

    public void addToUser(UserEntity u) {
        this.usuario = u;
        u.getPosts().add(this);
    }

    public void removeUser(UserEntity u) {
        u.getPosts().remove(this);
        this.usuario = null;
    }
}
