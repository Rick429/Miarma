package com.salesianostriana.dam.miarma.repository;

import com.salesianostriana.dam.miarma.model.Megusta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Megusta, Long> {
}
