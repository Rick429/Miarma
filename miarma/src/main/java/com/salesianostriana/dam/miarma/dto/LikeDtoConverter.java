package com.salesianostriana.dam.miarma.dto;

import com.salesianostriana.dam.miarma.model.Megusta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeDtoConverter {

    public GetLikeDto likeToGetLikeDto (Megusta like) {
        return GetLikeDto.builder()
                .post_id(like.getPost_id())
                .user_id(like.getUser_id())
                .build();
    }
}
