package com.salesianostriana.dam.miarma.users.dto;

import com.salesianostriana.dam.miarma.security.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    public LoginDto createUserDtoToLoginDto(CreateUserDto cu){
        return LoginDto.builder()
                .email(cu.getEmail())
                .password(cu.getPassword())
                .build();
    }

}