package com.salesianostriana.dam.miarma.repository;

import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTipopublicacion(Tipo tipo);

    List<Post> findByTipopublicacionAndUsuario(Tipo tipo, UserEntity user);

    List<Post> findByUsuario(UserEntity user);
}
