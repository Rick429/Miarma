package com.salesianostriana.dam.miarma.users.service;

import com.salesianostriana.dam.miarma.service.base.BaseService;
import com.salesianostriana.dam.miarma.users.dto.CreateUserDto;
import com.salesianostriana.dam.miarma.users.model.UserEntity;
import com.salesianostriana.dam.miarma.users.model.UserRole;
import com.salesianostriana.dam.miarma.users.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, UUID, UserEntityRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repositorio.findFirstByNick(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick + " no encontrado"));
    }


    public UserEntity save(CreateUserDto newUser, UserRole role) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            UserEntity userEntity = UserEntity.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .dateBirth(newUser.getDateBirth())
                    .nick(newUser.getNick())
                    .email(newUser.getEmail())
                    .role(role)
                    .build();
            return save(userEntity);
        } else {
            return null;
        }
    }

    public List<UserEntity> findUserByRole(UserRole userRole) { return repositorio.findUserByRole(userRole);}

}