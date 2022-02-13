package com.salesianostriana.dam.miarma.service.impl;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.repository.SolicitudRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public GetSolicitudDto saveSolicitud (UserEntity user, UserEntity u1) {
        Solicitud s = Solicitud.builder()
                .solicitado(u1)
                .solicitante(user)
                .build();

        s.addToSolicitante(user);
        s.addToSolicitado(u1);
        solicitudRepository.save(s);
        return GetSolicitudDto.builder()
                .solicitadoid(s.getSolicitado().getId())
                .solicitanteid(s.getSolicitante().getId())
                .build();
    }
}
