package com.salesianostriana.dam.miarma.users.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class GetUserDto {

    private UUID id;

    private String name;

    private String lastname;

    private String nick;

    private String email;

    private LocalDate datebirth;

    private String avatar;

    private boolean isPrivate;
}
