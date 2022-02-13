package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import com.salesianostriana.dam.miarma.repository.SolicitudRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void removeSolicitud (SolicitudPK solicitudPK) {
        Optional<Solicitud> s = solicitudRepository.findById(solicitudPK);

        if(s.isEmpty()){
            throw new SingleEntityNotFoundException(solicitudPK.toString(),Solicitud.class);
        } else {
            s.get().removeFromSolicitante(s.get().getSolicitante());
            s.get().removeFromSolicitado(s.get().getSolicitado());
        }
    }
}
