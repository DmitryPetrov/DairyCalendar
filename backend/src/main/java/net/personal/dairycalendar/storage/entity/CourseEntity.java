package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "course",
        uniqueConstraints = @UniqueConstraint(columnNames = {"app_user_id", "name"}))
public class CourseEntity extends BaseEntity{

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app_user_id")
    private AppUserEntity user;

    @OneToMany(mappedBy="course", fetch = FetchType.LAZY)
    private List<DayEntity> days;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Course_Tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<TagEntity> tags = new HashSet<>();

}
