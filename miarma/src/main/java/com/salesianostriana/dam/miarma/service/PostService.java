package com.salesianostriana.dam.miarma.service;

import com.salesianostriana.dam.miarma.dto.CreatePostDto;
import com.salesianostriana.dam.miarma.dto.GetPostDto;
import com.salesianostriana.dam.miarma.dto.PostDtoConverter;
import com.salesianostriana.dam.miarma.errors.exception.ListEntityNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import com.salesianostriana.dam.miarma.model.Tipo;
import com.salesianostriana.dam.miarma.repository.PostRepository;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final StorageService storageService;
    private final UserEntityService userEntityService;

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new SingleEntityNotFoundException(id.toString(), Post.class));
    }

    public Post save(CreatePostDto post, MultipartFile file, UserEntity user) {
        String uriThumb = "";
        String uri = "";
        if (file.getContentType().equals("video/mp4")) {
            uri = storageService.uploadVideo(file);
        } else {
            uri = storageService.uploadImage(file);

            if (file.getContentType().equals("image/jpeg")) {
                uriThumb = storageService.uploadResizeImage(file, 1024);
            }
        }

        return postRepository.save(postDtoConverter
                .createPostDtoToPost(post, uri, uriThumb, user));
    }

    public GetPostDto edit(CreatePostDto postDto, MultipartFile file, Long id) {
        Optional<Post> p = postRepository.findById(id);
        String name = p.get().getArchivo();
        String namethumb = p.get().getArchivoreescalado();
        if (p.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            if (!file.isEmpty()) {
                storageService.deleteFile(name);
                String uri;
                if (file.getContentType().equals("video/mp4")) {
                    uri = storageService.uploadVideo(file);
                    p.get().setArchivo(uri);
                    if (!namethumb.isEmpty()) {
                        storageService.deleteFile(namethumb);
                        p.get().setArchivoreescalado("");
                    }
                } else {
                    uri = storageService.uploadImage(file);
                    p.get().setArchivo(uri);

                    if (file.getContentType().equals("image/jpeg")) {
                        if (!namethumb.isEmpty())
                            storageService.deleteFile(namethumb);
                        String uriThumb = storageService.uploadResizeImage(file, 1024);
                        p.get().setArchivoreescalado(uriThumb);
                    }
                }
            }
            p.get().setTitulo(postDto.getTitulo());
            p.get().setDescripcion(postDto.getDescripcion());
            p.get().setTipopublicacion(postDto.getTipopublicacion());
            return postDtoConverter.postToGetPostDto(postRepository.save(p.get()));
        }
    }

    public List<GetPostDto> findAllPublic() {
        List<Post> lista = postRepository.findByTipopublicacion(Tipo.PUBLICA);
        if (lista.isEmpty()) {
            throw new ListEntityNotFoundException(Post.class);
        } else {
            return lista.stream().map(postDtoConverter::postToGetPostDto).collect(Collectors.toList());
        }
    }

    public void deleteById(Long id) {
        Optional<Post> p = postRepository.findById(id);
        if (p.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            p.get().removeUser(p.get().getUsuario());
            storageService.deleteFile(p.get().getArchivo());
            postRepository.deleteById(id);
        }
    }

    public List<GetPostDto> findPostsByNick(UserEntity user, String nick) {
        Optional<UserEntity> u1 = userEntityService.findFirstByNick(nick);
        if (u1.isEmpty()) {
            throw new SingleEntityNotFoundException(nick, UserEntity.class);
        } else {
            if (u1.get().getFollowers().contains(user)) {
                return u1.get().getPosts().stream()
                        .map(postDtoConverter::postToGetPostDto)
                        .collect(Collectors.toList());
            } else {
                return postRepository.findByTipopublicacionAndUsuario(Tipo.PUBLICA, u1.get())
                        .stream().map(postDtoConverter::postToGetPostDto)
                        .collect(Collectors.toList());
            }
        }

    }

    public List<GetPostDto> findAllPostUserLogged(UserEntity user) {
        return postRepository.findByUsuario(user).stream()
                .map(postDtoConverter::postToGetPostDto)
                .collect(Collectors.toList());
    }

}
