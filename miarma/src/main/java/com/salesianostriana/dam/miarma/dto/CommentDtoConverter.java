package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDtoConverter {

    public Comment createCommentDtoToComment(CreateCommentDto comment, UserEntity user, Long postid) {
        return Comment.builder()
                .post_id(postid)
                .user_id(user.getId())
                .comentario(comment.getComentario())
                .build();
    }

    public GetCommentDto commentToGetCommentDto (Comment c) {
        return GetCommentDto.builder()
                .comentario(c.getComentario())
                .post_id(c.getPost_id())
                .user_id(c.getUser_id())
                .build();
    }
}
