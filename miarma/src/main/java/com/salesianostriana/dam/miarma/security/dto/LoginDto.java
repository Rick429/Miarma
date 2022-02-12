package com.salesianostriana.dam.miarma.security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    private String nick;
    private String password;

}
