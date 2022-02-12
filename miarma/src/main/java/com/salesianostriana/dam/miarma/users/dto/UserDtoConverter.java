package com.salesianostriana.dam.miarma.users.dto;

import com.salesianostriana.dam.miarma.security.dto.LoginDto;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    public LoginDto createUserDtoToLoginDto(CreateUserDto cu){
        return LoginDto.builder()
                .nick(cu.getNick())
                .password(cu.getPassword())
                .build();
    }

    public GetUserDto UserEntityToGetUserDto(UserEntity u) {
        return GetUserDto.builder()
                .avatar(u.getAvatar())
                .name(u.getName())
                .lastname(u.getLastname())
                .email(u.getEmail())
                .nick(u.getNick())
                .datebirth(u.getDatebirth())
                .id(u.getId())
                .isPrivate(u.isIsprivate())
                .build();
    }

}