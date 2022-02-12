package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.CreatePostDto;
import com.salesianostriana.dam.miarma.dto.PostDtoConverter;
import com.salesianostriana.dam.miarma.errors.exception.ListEntityNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final StorageService storageService;

    public Post findById (Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new SingleEntityNotFoundException(id.toString(), Post.class));
    }

    public Post save (CreatePostDto post, MultipartFile file) {

        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
        
        return postRepository.save(postDtoConverter
                .createPostDtoToPost(post,uri));
    }

    public CreatePostDto edit (CreatePostDto postDto, Long id) {
        Optional<Post> p = postRepository.findById(id);
        if(p.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            return postDtoConverter.postToCreatePostDto(postRepository.save(p.get()));
        }
    }

    public List<Post> findAll() {
        List<Post> lista = postRepository.findAll();
        if(lista.isEmpty()){
            throw new ListEntityNotFoundException(Post.class);
        } else {
            return lista;
        }
    }

    public void deleteById (Long id) {
        Optional<Post> p = postRepository.findById(id);
        if(p.isEmpty()){
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            p.get().removeUser(p.get().getUsuario());
            postRepository.deleteById(id);
        }
    }



}
