package com.salesianostriana.dam.miarma.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class GetSolicitudDto {

    private UUID solicitanteid;
    private UUID solicitadoid;
}
