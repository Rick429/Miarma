package com.salesianostriana.dam.miarma.users.dto;

import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.validation.anotations.PasswordsMatch;
import com.salesianostriana.dam.miarma.validation.anotations.UniqueEmail;
import com.salesianostriana.dam.miarma.validation.anotations.UniqueNick;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder

@PasswordsMatch(passwordField = "password",
        verifyPasswordField = "password2",
        message = "{user.password.notmatch}")
public class CreateUserDto {

    @NotBlank
    @UniqueNick(message = "{user.unique.nick}")
    private String nick;
    private String name;
    private String lastname;
    @NotBlank
    @Email
    @UniqueEmail(message = "{user.unique.email}")
    private String email;
    @NotNull
    private Tipo tipocuenta;
    private String password;
    private String password2;
    @NotNull
    private LocalDate datebirth;
}
