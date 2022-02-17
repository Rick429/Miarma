package com.salesianostriana.dam.miarma.users.repository;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    @EntityGraph("usuario-posts")
    Optional<UserEntity> findFirstByNick(String nick);

    @EntityGraph("usuario-posts")
    Optional<UserEntity> findUserById(UUID id);

    @EntityGraph("usuario-posts")
    List<UserEntity> findUserByRole(UserRole userRole);

    boolean existsByNick(String nick);

    boolean existsByEmail(String email);

}
