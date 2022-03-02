package com.salesianostriana.dam.miarma.controller;

import com.salesianostriana.dam.miarma.dto.CreatePostDto;
import com.salesianostriana.dam.miarma.dto.GetPostDto;
import com.salesianostriana.dam.miarma.dto.PostDtoConverter;
import com.salesianostriana.dam.miarma.errors.exception.UnauthorizedException;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.service.PostService;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.utils.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostDtoConverter postDtoConverter;
    private final PaginationLinksUtils paginationLinksUtils;


    @Operation(summary = "Se crea un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se crea el post correctamente",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<GetPostDto> createPost (@Valid @RequestPart("post") CreatePostDto c,
                                                     @RequestPart("file")MultipartFile file,
                                                     @AuthenticationPrincipal UserEntity user) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postDtoConverter.postToGetPostDto(postService.save(c, file, user)));
    }

    @Operation(summary = "Se edita un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se edita el post correctamente",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public GetPostDto edit(@Valid @RequestPart("post") CreatePostDto c,
                              @RequestPart("file")MultipartFile file, @PathVariable Long id) {
        return postService.edit(c, file, id);
    }

    @Operation(summary = "Se elimina un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se elimina el post",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra la vivienda con ese id",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Se muestran todos los posts públicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se devuelve una lista con todos los posts públicos",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La lista esta vacia",
                    content = @Content),
    })
    @GetMapping("/public")
    public ResponseEntity<Page<GetPostDto>> findAllPublic (Pageable pageable, HttpServletRequest request) {
        Page<GetPostDto> pagPostDto = postService.findAllPublic(pageable);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(pagPostDto, uriBuilder)).body(pagPostDto);
    }

    @Operation(summary = "Se busca un post por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Devuelve un post",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha podido encontrar un post con ese id",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public GetPostDto findById (@AuthenticationPrincipal UserEntity user,
                                @PathVariable Long id) {
        Post p1 = postService.findById(id);
        if(p1.getUsuario().getId().equals(user.getId()) || p1.getTipopublicacion().equals(Tipo.PUBLICA)
        || user.getFollowing().contains(p1.getUsuario())){
            return postDtoConverter.postToGetPostDto(postService.findById(id));
        } else {
            throw new UnauthorizedException("No tiene permisos para ver este post");
        }
    }

    @Operation(summary = "lista de los posts de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se devuelve la lista de posts del usuario con el nick dado",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado un usuario con ese nick",
                    content = @Content),
    })
    @GetMapping("/user/{nick}")
    public ResponseEntity<Page<GetPostDto>> findAllPostByNick (@AuthenticationPrincipal UserEntity user,
                                         @PathVariable String nick, Pageable pageable, HttpServletRequest request) {

        Page<GetPostDto> pagPostDto = postService.findPostsByNick(user, nick, pageable);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(pagPostDto, uriBuilder)).body(pagPostDto);
    }

    @Operation(summary = "Listar todos los posts del usario logueado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Devuelve una lista con todos los posts del usuario logueado",
                    content = {@Content(mediaType = "aplication/json",
                            schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no tiene posts",
                    content = @Content),
    })
    @GetMapping("/me")
    public List<GetPostDto> findAllPostUserLogged (@AuthenticationPrincipal UserEntity user) {
        return postService.findAllPostUserLogged(user);
    }
}
