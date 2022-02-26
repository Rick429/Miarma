package com.salesianostriana.dam.miarma.model;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID user_id;
    private String comentario;
    private String nick;
    private String image;


    @ManyToOne
    private Post postcomentado;

    public void addToPost(Post p) {
        this.postcomentado = p;
        p.getComentarios().add(this);
    }

    public void removePost(Post p) {
        p.getComentarios().remove(this);
        this.postcomentado = null;
    }
}
