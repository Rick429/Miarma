package com.salesianostriana.dam.miarma.security;

import com.salesianostriana.dam.miarma.security.dto.JwtUserResponse;
import com.salesianostriana.dam.miarma.security.dto.LoginDto;
import com.salesianostriana.dam.miarma.security.jwt.JwtProvider;
import com.salesianostriana.dam.miarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.miarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @Operation(summary = "Se hace login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se hace login",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getEmail(),
                                loginDto.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);


        UserEntity user = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertUserToJwtUserResponse(user, jwt));

    }

    @GetMapping("/me")
    public ResponseEntity<?> quienSoyYo(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(convertUserToJwtUserResponse(user, null));
    }


    private JwtUserResponse convertUserToJwtUserResponse(UserEntity user, String jwt) {
        return JwtUserResponse.builder()
                .nick(user.getNick())
                .name(user.getName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .token(jwt)
                .build();
    }

    @Operation(summary = "Registra nuevo usuario y hace login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se registra el usuario y se hace login",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PostMapping("/auth/register/{role}")
    public ResponseEntity<?> nuevoUsuario(@RequestBody CreateUserDto newUser, @PathVariable String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());
        UserEntity saved = userEntityService.save(newUser, userRole);
        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(this.login(userDtoConverter.createUserDtoToLoginDto(newUser)));
    }
}