package com.salesianostriana.dam.miarma.repository;


import com.salesianostriana.dam.miarma.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
