package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.personal.dairycalendar.service.AppUser;
import org.springframework.security.core.GrantedAuthority;

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

    public AppUser toUser() {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new AppUser(
                id,
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
