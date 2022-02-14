package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Solicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudDtoConverter {
    
    public GetSolicitudDto solicitudToGetSolicitudDto (Solicitud solicitud) {
        return GetSolicitudDto.builder()
                .solicitadoid(solicitud.getSolicitado().getId())
                .solicitanteid(solicitud.getSolicitante().getId())
                .build();
    }
}
