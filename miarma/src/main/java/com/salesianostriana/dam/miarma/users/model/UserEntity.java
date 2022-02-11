package com.salesianostriana.dam.miarma.users.model;

import com.salesianostriana.dam.miarma.model.Post;
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
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
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

    private String lastName;

    private String nick;

    private String email;

    private LocalDate dateBirth;

    private String avatar;

    private boolean isPrivate;

    private UserRole role;

    private String password;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    private List<UserEntity> followers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name="FK_FOLLOWING_USER")),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name="FK_USER_FOLLOW")),
            name = "follower"
    )
    private List<UserEntity> following = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "usuario")
    private List<Post> posts = new ArrayList<>();

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
        this.getFollowing().add(this);
    }

    public void removeAsignatura(UserEntity u) {
        this.getFollowing().remove(u);
        this.getFollowers().remove(u);
    }


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
