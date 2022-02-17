package com.salesianostriana.dam.miarma.controller;

import com.salesianostriana.dam.miarma.dto.CreateCommentDto;
import com.salesianostriana.dam.miarma.dto.GetCommentDto;
import com.salesianostriana.dam.miarma.service.CommentService;
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
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Crear un comentario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se crea el comentario correctamente",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PostMapping("/{id}")
    public ResponseEntity<GetCommentDto> createComment(@Valid @RequestPart("comment") CreateCommentDto c,
                                                       @AuthenticationPrincipal UserEntity user,
                                                       @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(c, user, id));
    }

    @Operation(summary = "Eliminar un comentario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina el comentario correctamente",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el comentario con ese id",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal UserEntity user,
                                        @PathVariable Long id) {
        commentService.delete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
