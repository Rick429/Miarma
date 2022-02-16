package com.salesianostriana.dam.miarma.model;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Comment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private UUID user_id;
    private Long post_id;
    private String comentario;


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
