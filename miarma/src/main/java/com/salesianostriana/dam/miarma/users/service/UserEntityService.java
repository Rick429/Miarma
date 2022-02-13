package com.salesianostriana.dam.miarma.users.service;

import com.salesianostriana.dam.miarma.dto.GetSolicitudDto;
import com.salesianostriana.dam.miarma.errors.exception.SingleEntityNotFoundException;
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
                .orElseThrow(()-> new UsernameNotFoundException(nick + " no encontrado"));
    }


    public UserEntity save(CreateUserDto newUser, MultipartFile avatar) {
            String uri = storageService.uploadImage(avatar);
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

    public List<UserEntity> findUserByRole(UserRole userRole) { return repositorio.findUserByRole(userRole);}

    public UserEntity findById (UUID id){
        return repositorio.findById(id)
                .orElseThrow(() -> new SingleEntityNotFoundException(id.toString(),UserEntity.class));
    }

    public GetUserDto edit (CreateUserDto editUser,UserEntity user, MultipartFile avatar) {
            String uri = storageService.uploadImage(avatar);
            user.setName(editUser.getName());
            user.setLastname(editUser.getLastname());
            user.setAvatar(uri);
            user.setPassword(editUser.getPassword());
            user.setDatebirth(editUser.getDatebirth());
            user.setTipocuenta(editUser.getTipocuenta());
            user.setEmail(editUser.getEmail());
            user.setNick(editUser.getNick());
            return userDtoConverter.UserEntityToGetUserDto(repositorio.save(user));
        }

        public Optional<UserEntity> findFirstByNick(String nick){return repositorio.findFirstByNick(nick);}

    public GetSolicitudDto followUser (UserEntity user, String nick) {

        Optional<UserEntity> u1 = findFirstByNick(nick);
        if(u1.isEmpty()){
            throw new SingleEntityNotFoundException(nick,UserEntity.class);
        } else {

            GetSolicitudDto solicitud = solicitudService.saveSolicitud(user, u1.get());
            return solicitud;
        }
    }

    public ResponseEntity<?> acceptFollow (UserEntity user, SolicitudPK solicitudPK) {
        UserEntity solicitante = findById(solicitudPK.getSolicitante_id());
        user.addFollower(solicitante);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<?> declineFollow (SolicitudPK solicitudPK) {
        solicitudService.removeSolicitud(solicitudPK);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}



