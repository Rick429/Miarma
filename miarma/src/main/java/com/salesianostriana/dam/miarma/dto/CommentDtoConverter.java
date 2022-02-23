package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDtoConverter {

    public Comment createCommentDtoToComment(CreateCommentDto comment, UserEntity user, String uri) {
        return Comment.builder()
                .user_id(user.getId())
                .comentario(comment.getComentario())
                .nick(user.getNick())
                .image(uri)
                .build();
    }

    public GetCommentDto commentToGetCommentDto (Comment c) {
        return GetCommentDto.builder()
                .comentario(c.getComentario())
                .post_id(c.getPostcomentado().getId())
                .user_id(c.getUser_id())
                .nick(c.getNick())
                .image(c.getImage())
                .build();
    }
}
