package com.salesianostriana.dam.miarma.users.controller;

import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UserController {

    private final UserEntityService userEntityService;

    @GetMapping("profile/{id}")
    public UserEntity findById() {
        return UserEntity.builder().build();
    }

    @PutMapping("profile/me")
    public GetUserDto edit () {
        return GetUserDto.builder().build();
    }

    @PostMapping("follow/{nick}")
    public GetUserDto followUser () {
        return GetUserDto.builder().build();
    }

    @PostMapping("follow/accept/{id}")
    public GetUserDto acceptFollow () {
        return GetUserDto.builder().build();
    }

    @PostMapping("follow/decline/{id}")
    public GetUserDto declineFollow () {
        return GetUserDto.builder().build();
    }

    @GetMapping("follow/list")
    public List<Solicitud> findAllSolicitudes () {
        return List.of(Solicitud.builder().build());
    }

}
