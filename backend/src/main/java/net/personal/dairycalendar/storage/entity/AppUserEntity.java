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
@Table(name = "app_user")
public class AppUserEntity extends BaseEntity {

    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy="user", orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<CourseEntity> courses;

    @ElementCollection
    @CollectionTable(name = "order_course",
            joinColumns = {@JoinColumn(name = "app_user_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "course_id")
    @Column(name = "number")
    private Map<Long, Integer> coursesOrder;
}
