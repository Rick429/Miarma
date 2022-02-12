package com.salesianostriana.dam.miarma.controller;

import com.salesianostriana.dam.miarma.dto.CreatePostDto;
import com.salesianostriana.dam.miarma.dto.PostDtoConverter;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.service.PostService;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CreatePostDto> createPost (@Valid @RequestPart("post") CreatePostDto c,
                                            @RequestPart("file")MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postDtoConverter.postToCreatePostDto(postService.save(c, file)));
    }

    @PutMapping("/{id}")
    public CreatePostDto edit(@Valid @RequestPart CreatePostDto c, @PathVariable Long id) {
        return postService.edit(c, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/public")
    public List<Post> findAll () {
        return List.of(Post.builder().build());
    }

    @GetMapping("/{id}")
    public GetUserDto findById () {
        return GetUserDto.builder().build();
    }

    @GetMapping("/{nick}")
    public List<Post> findAllPostByNick () {
        return List.of(Post.builder().build());
    }

    @GetMapping("/me")
    public List<Post> findAllPostUserLogged () {
        return List.of(Post.builder().build());
    }
}
