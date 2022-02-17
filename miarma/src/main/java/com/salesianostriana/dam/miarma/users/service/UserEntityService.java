package com.salesianostriana.dam.miarma.users.service;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.errors.exception.FollowUserException;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.SolicitudPK;
import com.salesianostriana.dam.miarma.service.StorageService;
import com.salesianostriana.dam.miarma.service.SolicitudService;
import com.salesianostriana.dam.miarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.miarma.users.dto.GetUserDto;
import com.salesianostriana.dam.miarma.users.dto.UserDtoConverter;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import com.salesianostriana.dam.miarma.users.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository repositorio;
    private final UserDtoConverter userDtoConverter;
    private final StorageService storageService;
    private final SolicitudService solicitudService;

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repositorio.findFirstByNick(nick)
                .orElseThrow(() -> new UsernameNotFoundException(nick + " no encontrado"));
    }

    public UserEntity save(CreateUserDto newUser, MultipartFile avatar) {
        String uri = storageService.uploadResizeImage(avatar, 128);
        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode(newUser.getPassword()))
                .avatar(uri)
                .name(newUser.getName())
                .lastname(newUser.getLastname())
                .datebirth(newUser.getDatebirth())
                .nick(newUser.getNick())
                .email(newUser.getEmail())
                .tipocuenta(newUser.getTipocuenta())
                .role(UserRole.USER)
                .build();
        return repositorio.save(userEntity);
    }

    public List<UserEntity> findUserByRole(UserRole userRole) {
        return repositorio.findUserByRole(userRole);
    }

    public UserEntity findById(UUID id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new SingleEntityNotFoundException(id.toString(), UserEntity.class));
    }

    public GetUserDto edit(CreateUserDto editUser, UserEntity user, MultipartFile avatar) {

        if(!avatar.isEmpty()){
            storageService.deleteFile(user.getAvatar());
            String uri = storageService.uploadResizeImage(avatar, 128);
            user.setAvatar(uri);
        }
        user.setName(editUser.getName());
        user.setLastname(editUser.getLastname());
        user.setPassword(editUser.getPassword());
        user.setDatebirth(editUser.getDatebirth());
        user.setTipocuenta(editUser.getTipocuenta());
        user.setEmail(editUser.getEmail());
        user.setNick(editUser.getNick());
        return userDtoConverter.UserEntityToGetUserDto(repositorio.save(user));
    }

    public Optional<UserEntity> findFirstByNick(String nick) {
        return repositorio.findFirstByNick(nick);
    }

    public GetSolicitudDto followUser(UserEntity user, String nick) {

        Optional<UserEntity> u1 = findFirstByNick(nick);
        Optional<UserEntity> u2 = findFirstByNick(user.getNick());
        SolicitudPK pk = new SolicitudPK(u1.get().getId(), user.getId());

        if (u1.isEmpty()) {
            throw new SingleEntityNotFoundException(nick, UserEntity.class);
        } else {
            Optional<Solicitud> solicitud1 = solicitudService.findById(pk);

                if (u1.get().getFollowers().contains(u2.get())) {
                    throw new FollowUserException("Usted ya esta siguiendo a este usuario");
                }

            if (u1.get().getId().equals(u2.get().getId())) {
                throw new FollowUserException("No puede seguirse a usted mismo");
            } else if (!solicitud1.isEmpty()) {
                throw new FollowUserException("Ya ha enviado una solicitud al usuario: " + nick);
            } else {
                GetSolicitudDto solicitud = solicitudService.saveSolicitud(u2.get(), u1.get());
                return solicitud;
            }
        }
    }

    public ResponseEntity<?> acceptFollow(UserEntity user, UUID id) {

        Optional<UserEntity> solicitado = repositorio.findUserById(user.getId());
        Optional<UserEntity> solicitante = repositorio.findUserById(id);
        SolicitudPK s = new SolicitudPK(solicitado.get().getId(), id);
        Optional<Solicitud> s1 = solicitudService.findById(s);
        if (s1.isEmpty()) {
            throw new SingleEntityNotFoundException(s.toString(), Solicitud.class);
        } else {
            if (s1.get().getSolicitado().getId().equals(solicitado.get().getId())
                    && s1.get().getSolicitante().getId().equals(solicitante.get().getId())) {
                solicitado.get().addFollower(solicitante.get());
                repositorio.save(solicitado.get());
                repositorio.save(solicitante.get());
                solicitudService.removeSolicitud(solicitado.get().getId(), id);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                throw new SingleEntityNotFoundException(s.toString(), Solicitud.class);
            }
        }
    }

    public ResponseEntity<?> declineFollow(UserEntity user, UUID id) {
        SolicitudPK s = new SolicitudPK(user.getId(), id);
        UserEntity solicitante = findById(id);
        Optional<Solicitud> s1 = solicitudService.findById(s);
        if (s1.isEmpty()) {
            throw new SingleEntityNotFoundException(s.toString(), Solicitud.class);
        } else {
            if (s1.get().getSolicitado().getId().equals(user.getId())
                    && s1.get().getSolicitante().getId().equals(solicitante.getId())) {
                solicitudService.removeSolicitud(user.getId(), id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                throw new SingleEntityNotFoundException(s.toString(), Solicitud.class);
            }
        }
    }
}



