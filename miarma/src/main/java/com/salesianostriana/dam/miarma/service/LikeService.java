package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.*;
import com.salesianostriana.dam.miarma.model.Megusta;
import com.salesianostriana.dam.miarma.repository.LikeRepository;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeDtoConverter likeDtoConverter;

    public GetLikeDto save (UserEntity user, Long postid) {
        Megusta l1 = Megusta.builder()
                .post_id(postid)
                .user_id(user.getId())
                .build();
        likeRepository.save(l1);
        return likeDtoConverter.likeToGetLikeDto(l1);
    }
}
