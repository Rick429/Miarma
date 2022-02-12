package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDtoConverter {

    public CreatePostDto postToCreatePostDto(Post post) {
        return CreatePostDto.builder()
                .titulo(post.getTitulo())
                .descripcion(post.getDescripcion())
                .archivo(post.getArchivo())
                .build();
    }

    public Post createPostDtoToPost (CreatePostDto createPostDto, String uri) {
        return Post.builder()
                .titulo(createPostDto.getTitulo())
                .descripcion(createPostDto.getDescripcion())
                .archivo(uri)
                .build();
    }
}
