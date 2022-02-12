package com.salesianostriana.dam.miarma.users.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class CreateUserDto {

    private String nick;
    private String avatar;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String password2;
    private LocalDate datebirth;
}
