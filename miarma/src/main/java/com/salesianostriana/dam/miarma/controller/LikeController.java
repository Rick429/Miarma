package com.salesianostriana.dam.miarma.controller;

import com.salesianostriana.dam.miarma.dto.CreateCommentDto;
import com.salesianostriana.dam.miarma.dto.GetCommentDto;
import com.salesianostriana.dam.miarma.dto.GetLikeDto;
import com.salesianostriana.dam.miarma.service.LikeService;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
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
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Like a un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se da el like al post correctamente",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PostMapping("/{id}")
    public ResponseEntity<GetLikeDto> createLike (@AuthenticationPrincipal UserEntity user,
                                                     @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeService.save(user, id));
    }
}
