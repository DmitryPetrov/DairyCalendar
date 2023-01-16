package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app-user")
public class AppUserEntity extends BaseEntity {

    @Column(name = "username", unique = true, length = 255)
    private String username;
    @Column(name = "password", length = 255)
    private String password;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy="user", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<CourseEntity> courses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

/*
    @ElementCollection
    @CollectionTable(name = "order_course",
            joinColumns = {@JoinColumn(name = "app-user_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "course_id")
    @Column(name = "number")
    private Map<Long, Integer> coursesOrder;*/

    public User toUser() {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new User(
                username,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                this.getGrantedAuthority()
        );
    }

    public Set<GrantedAuthority> getGrantedAuthority() {
        return Set.of(this.getRole().getTitle().getGrantedAuthority());
    }
}
