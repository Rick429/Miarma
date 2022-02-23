package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.CommentDtoConverter;
import com.salesianostriana.dam.miarma.dto.CreateCommentDto;
import com.salesianostriana.dam.miarma.dto.GetCommentDto;
import com.salesianostriana.dam.miarma.errors.exception.CommentException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Comment;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.repository.CommentRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Value("${image.thumbnail.width:512}")
    private int thumbnailWidth;

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final PostService postService;
    private final StorageService storageService;

    public GetCommentDto save (CreateCommentDto comment, UserEntity user, Long postid, MultipartFile file) {
        Post p = postService.findById(postid);
        String uriThumb="";
        if (file.getContentType().equals("image/jpeg") ||
                file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/gif")) {
            uriThumb = storageService.uploadResizeImage(file, thumbnailWidth);
        }
        Comment c1 = commentDtoConverter.createCommentDtoToComment(comment, user, uriThumb);
        c1.addToPost(p);
        commentRepository.save(c1);
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
