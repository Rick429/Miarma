package com.salesianostriana.dam.miarma.users.model;

import com.salesianostriana.dam.miarma.model.Post;
import com.salesianostriana.dam.miarma.model.Solicitud;
import com.salesianostriana.dam.miarma.model.Tipo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String lastname;

    @NaturalId
    private String nick;

    private String email;

    private LocalDate datebirth;

    private String avatar;

    private Tipo tipocuenta;

    private UserRole role;

    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY)
    private List<UserEntity> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserEntity> following = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "solicitado", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Solicitud> solicitudes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "solicitante", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Solicitud> solicitados = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public void addFollower(UserEntity u) {
        if (this.getFollowers() == null)
            this.setFollowers(new ArrayList<>());
        this.getFollowers().add(u);

        if (this.getFollowing() == null)
            this.setFollowing(new ArrayList<>());
        u.getFollowing().add(this);
    }

    public void removeFollower(UserEntity u) {
        this.getFollowers().remove(u);
        u.getFollowing().remove(this);
    }

    @Override
    public String getUsername() {
        return nick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
