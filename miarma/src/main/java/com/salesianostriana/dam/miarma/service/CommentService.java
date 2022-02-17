package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.CommentDtoConverter;
import com.salesianostriana.dam.miarma.dto.CreateCommentDto;
import com.salesianostriana.dam.miarma.dto.GetCommentDto;
import com.salesianostriana.dam.miarma.errors.exception.CommentException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.repository.CommentRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;

    public GetCommentDto save (CreateCommentDto comment, UserEntity user, Long postid) {
            Comment c1 = commentRepository.save(commentDtoConverter.createCommentDtoToComment(comment, user, postid));
        return commentDtoConverter.commentToGetCommentDto(c1);
    }

    public void delete (Long id, UserEntity user) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()){
            throw new SingleEntityNotFoundException(id.toString(), Comment.class);
        }else {
            if(comment.get().getUser_id().equals(user.getId())){
                commentRepository.delete(comment.get());
            }else{
                throw new CommentException("Solo el usuario que creo el comentario puede eliminarlo");
            }

        }
    }

}
