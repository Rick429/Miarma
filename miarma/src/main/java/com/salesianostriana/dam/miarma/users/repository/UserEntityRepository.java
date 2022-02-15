package com.salesianostriana.dam.miarma.users.repository;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByNick(String nick);

    Optional<UserEntity> findUserByNick(String nick);

    public List<UserEntity> findUserByRole(UserRole userRole);

    boolean existsByNick(String nick);

//    @Query(value = """
//            select * from users u
//            where follower in u.followers
//            """)
//    public boolean isFollower(UserEntity follower);

    boolean existsByEmail(String email);
}
