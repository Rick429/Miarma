package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostDtoConverter {

    private final CommentDtoConverter commentDtoConverter;

    public CreatePostDto postToCreatePostDto(Post post) {
        return CreatePostDto.builder()
                .titulo(post.getTitulo())
                .descripcion(post.getDescripcion())
                .tipopublicacion(post.getTipopublicacion())
                .build();
    }

    public Post createPostDtoToPost (CreatePostDto createPostDto, String uri, String urithumb,UserEntity user) {
        return Post.builder()
                .titulo(createPostDto.getTitulo())
                .descripcion(createPostDto.getDescripcion())
                .archivo(uri)
                .tipopublicacion(createPostDto.getTipopublicacion())
                .archivoreescalado(urithumb)
                .usuario(user)
                .build();
    }

    public GetPostDto postToGetPostDto (Post post) {
        return GetPostDto.builder()
                .id(post.getId())
                .titulo(post.getTitulo())
                .descripcion(post.getDescripcion())
                .archivo(post.getArchivo())
                .archivoreescalado(post.getArchivoreescalado())
                .tipopublicacion(post.getTipopublicacion())
                .numlikes(post.getLikes().size())
                .commentarios(post.getComentarios().stream()
                        .map(commentDtoConverter::commentToGetCommentDto)
                        .collect(Collectors.toList()))
                .userid(post.getUsuario().getId())
                .build();
    }

}
