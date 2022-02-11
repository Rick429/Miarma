package com.salesianostriana.dam.miarma.model;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.*;
import javax.persistence.*;
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

    @ManyToOne
    private UserEntity usuario;

    public void addToUser(UserEntity u) {
        this.usuario = u;
        u.getPosts().add(this);
    }

    public void removeUser(UserEntity u) {
        u.getPosts().remove(this);
        this.usuario = null;
    }
}
