package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "AppUser")
public class AppUserEntity extends BaseEntity {

    @Column(unique = true)
    private String login;
    private byte[] password;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy="user", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<CourseEntity> courses;

    @ElementCollection
    @CollectionTable(name = "order_course",
            joinColumns = {@JoinColumn(name = "appUser_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "course_id")
    @Column(name = "number")
    private Map<Long, Integer> coursesOrder;
}
