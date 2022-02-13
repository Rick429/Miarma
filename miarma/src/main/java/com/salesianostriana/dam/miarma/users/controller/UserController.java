package com.salesianostriana.dam.miarma.users.controller;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.UnauthorizedException;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @GetMapping("profile/{id}")
    public GetUserDto findById(@AuthenticationPrincipal UserEntity user,@PathVariable UUID id) {
        UserEntity u1 = userEntityService.findById(id);
        if(u1.getFollowers().contains(user) || u1.getTipocuenta().equals(Tipo.PUBLICA)){
            return userDtoConverter.UserEntityToGetUserDto(u1);
        } else {
            throw new UnauthorizedException("La cuenta a la que intentas acceder es privada");
        }

    }

    @PutMapping("profile/me")
    public GetUserDto edit (@RequestPart("user") CreateUserDto editUser,
                            @RequestPart("avatar") MultipartFile avatar,
                            @AuthenticationPrincipal UserEntity user) {
        return userEntityService.edit(editUser,user, avatar);
    }

    @PostMapping("follow/{nick}")
    public GetSolicitudDto followUser (@AuthenticationPrincipal UserEntity user,
                                       @PathVariable String nick) {
        return userEntityService.followUser(user, nick);
    }

    @PostMapping("follow/accept/{id}")
    public ResponseEntity<?> acceptFollow (@AuthenticationPrincipal UserEntity user,
                                        @RequestPart("solicitud") SolicitudPK solicitudPK ) {

        return userEntityService.acceptFollow(user, solicitudPK);
    }

    @PostMapping("follow/decline/{id}")
    public ResponseEntity<?> declineFollow () {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("follow/list")
    public List<Solicitud> findAllSolicitudes () {
        return List.of(Solicitud.builder().build());
    }

}
