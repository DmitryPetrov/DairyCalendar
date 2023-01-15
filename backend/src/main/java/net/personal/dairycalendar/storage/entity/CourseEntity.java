package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "course",
        uniqueConstraints = @UniqueConstraint(columnNames = {"app-user_id", "title"}))
public class CourseEntity extends BaseEntity{

    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id")
    private AppUserEntity user;

    @OneToMany(mappedBy="course", fetch = FetchType.LAZY)
    private List<DayEntity> days;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tag-collection_id", unique = true)
    private TagCollectionEntity tagCollection;

}
