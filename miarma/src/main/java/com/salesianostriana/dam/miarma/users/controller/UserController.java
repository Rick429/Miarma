package com.salesianostriana.dam.miarma.users.controller;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.UnauthorizedException;
import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.service.SolicitudService;
import com.salesianostriana.dam.miarma.users.dto.EditUserDto;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import com.salesianostriana.dam.miarma.utils.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;
    private final SolicitudService solicitudService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Operation(summary = "Usuario por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se busca un usuario por su id",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe un usuario con ese id",
                    content = @Content),
    })
    @GetMapping("profile/{id}")
    public GetUserDto findById(@AuthenticationPrincipal UserEntity user,@PathVariable UUID id) {
        UserEntity u1 = userEntityService.findById(id);
        if(u1.getFollowers().contains(user) || u1.getTipocuenta().equals(Tipo.PUBLICA)){
            return userDtoConverter.UserEntityToGetUserDto(u1);
        } else {
            throw new UnauthorizedException("La cuenta a la que intentas acceder es privada");
        }

    }

    @Operation(summary = "Editar perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se edita el perfil de un usuario",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PutMapping("profile/me")
    public GetUserDto edit (@RequestPart("user") @Valid EditUserDto editUser,
                            @RequestPart("avatar") MultipartFile avatar,
                            @AuthenticationPrincipal UserEntity user) {
        return userEntityService.edit(editUser,user, avatar);
    }

    @Operation(summary = "Seguir a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se envia una solicitud de seguimiento a un usuario",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe un usuario con ese nick",
                    content = @Content),
    })
    @PostMapping("follow/{nick}")
    public GetSolicitudDto followUser (@AuthenticationPrincipal UserEntity user,
                                       @PathVariable String nick) {
        return userEntityService.followUser(user, nick);
    }

    @Operation(summary = "Aceptar follow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se acepta una solicitud de seguimiento",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe una solicitud",
                    content = @Content),
    })
    @PostMapping("follow/accept/{id}")
    public ResponseEntity<?> acceptFollow (@AuthenticationPrincipal UserEntity user,
                                        @PathVariable UUID id) {

        return userEntityService.acceptFollow(user, id);
    }

    @Operation(summary = "Rechazar follow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se rechaza una solicitud de seguimiento",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe una solicitud",
                    content = @Content),
    })
    @PostMapping("follow/decline/{id}")
    public ResponseEntity<?> declineFollow (@AuthenticationPrincipal UserEntity user,
                                            @PathVariable UUID id ) {
        userEntityService.declineFollow(user, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Listar todas las solicitudes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan todas las solicitudes que tiene un usuario en ese momento",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No hay solicitudes",
                    content = @Content),
    })
    @GetMapping("follow/list")
    public List<GetSolicitudDto> findAllSolicitudes (@AuthenticationPrincipal UserEntity user) {
        return solicitudService.findAll(user.getId());
    }

    @PutMapping("giveadmin/{id}")
    public ResponseEntity<?> giveAdminRol(@AuthenticationPrincipal UserEntity user,@PathVariable UUID id) {
        userEntityService.giveAdminRol(user, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    public ResponseEntity<Page<GetUserDto>> findAllUsers (@AuthenticationPrincipal UserEntity user, Pageable pageable, HttpServletRequest request) {
        Page<GetUserDto> pagUserDto = userEntityService.findAllUsers(user, pageable);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(pagUserDto, uriBuilder)).body(pagUserDto);
    }
}
