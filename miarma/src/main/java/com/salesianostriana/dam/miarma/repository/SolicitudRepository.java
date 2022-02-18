package com.salesianostriana.dam.miarma.repository;

import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, SolicitudPK> {

    @EntityGraph("usuario-solicitud")
    Optional<Solicitud> findSolicitudById(SolicitudPK id);
}
