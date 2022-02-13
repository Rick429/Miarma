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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostDtoConverter postDtoConverter;

    @PostMapping("/")
    public ResponseEntity<GetPostDto> createPost (@Valid @RequestPart("post") CreatePostDto c,
                                                     @RequestPart("file")MultipartFile file,
                                                     @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postDtoConverter.postToGetPostDto(postService.save(c, file, user)));
    }

    @PutMapping("/{id}")
    public GetPostDto edit(@Valid @RequestPart("post") CreatePostDto c,
                              @RequestPart("file")MultipartFile file, @PathVariable Long id) {
        return postService.edit(c, file, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/public")
    public List<GetPostDto> findAllPublic () {
        return postService.findAllPublic();
    }

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

    @GetMapping("/user/{nick}")
    public List<GetPostDto> findAllPostByNick (@AuthenticationPrincipal UserEntity user,
                                         @PathVariable String nick) {

        return postService.findPostsByNick(user, nick);
    }

    @GetMapping("/me")
    public List<GetPostDto> findAllPostUserLogged (@AuthenticationPrincipal UserEntity user) {
        return postService.findAllPostUserLogged(user);
    }
}
