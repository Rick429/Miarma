package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.CommentDtoConverter;
import com.salesianostriana.dam.miarma.dto.CreateCommentDto;
import com.salesianostriana.dam.miarma.dto.CreatePostDto;
import com.salesianostriana.dam.miarma.dto.GetCommentDto;
import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.repository.CommentRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;

    public GetCommentDto save (CreateCommentDto comment, UserEntity user, Long postid) {
            Comment c1 = commentRepository.save(commentDtoConverter.createCommentDtoToComment(comment, user, postid));
        return commentDtoConverter.commentToGetCommentDto(c1);
    }

}
