package com.salesianostriana.dam.miarma.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Megusta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID user_id;

    @ManyToOne
    private Post postlikeado;

    public void addToPost(Post p) {
        this.postlikeado = p;
        p.getLikes().add(this);
    }

    public void removeFromPost(Post p) {
        p.getLikes().remove(this);
        this.postlikeado = null;
    }
}
