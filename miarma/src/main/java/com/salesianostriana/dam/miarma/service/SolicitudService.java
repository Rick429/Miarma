package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.dto.SolicitudDtoConverter;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import com.salesianostriana.dam.miarma.repository.SolicitudRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final SolicitudDtoConverter solicitudDtoConverter;

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

    public void removeSolicitud (UUID userid, UUID id) {
        SolicitudPK solicitudPK = new SolicitudPK(userid, id);
        Optional<Solicitud> s = solicitudRepository.findById(solicitudPK);

        if(s.isEmpty()){
            throw new SingleEntityNotFoundException(id.toString(),Solicitud.class);
        } else {
            s.get().removeFromSolicitante(s.get().getSolicitante());
            s.get().removeFromSolicitado(s.get().getSolicitado());
            solicitudRepository.delete(s.get());
        }
    }

    public List<GetSolicitudDto> findAll () {
        return solicitudRepository.findAll().stream()
                .map(solicitudDtoConverter::solicitudToGetSolicitudDto)
                .collect(Collectors.toList());
    }

    public Optional<Solicitud> findById (SolicitudPK s) {
        return solicitudRepository.findById(s);
    }
}
