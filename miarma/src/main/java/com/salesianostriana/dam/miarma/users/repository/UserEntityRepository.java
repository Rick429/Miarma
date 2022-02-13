package com.salesianostriana.dam.miarma.users.repository;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByNick(String nick);

    Optional<UserEntity> findUserByNick(String nick);

    public List<UserEntity> findUserByRole(UserRole userRole);

    boolean existsByNick(String nick);

    boolean existsByEmail(String email);
}
