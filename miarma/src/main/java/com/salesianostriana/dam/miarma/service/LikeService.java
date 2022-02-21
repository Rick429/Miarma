package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.*;
import com.salesianostriana.dam.miarma.errors.exception.MegustaException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Megusta;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.repository.LikeRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeDtoConverter likeDtoConverter;
    private final PostService postService;

    public GetLikeDto save(UserEntity user, Long postid) {
        Post p = postService.findById(postid);
        Megusta l1 = Megusta.builder()
                .user_id(user.getId())
                .build();
        l1.addToPost(p);
        likeRepository.save(l1);
        return likeDtoConverter.likeToGetLikeDto(l1);
    }

    public void delete(Long id, UserEntity user) {
        Optional<Megusta> megusta = likeRepository.findById(id);
        if (megusta.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Megusta.class);
        } else {
            if (megusta.get().getUser_id().equals(user.getId())) {
                likeRepository.delete(megusta.get());
            } else {
                throw new MegustaException("Solo el usuario que dio el like puede quitarlo");
            }

        }
    }
}
