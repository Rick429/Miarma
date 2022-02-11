package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.repository.PostRepository;
import com.salesianostriana.dam.miarma.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService extends BaseService<Post, Long, PostRepository> {


}
