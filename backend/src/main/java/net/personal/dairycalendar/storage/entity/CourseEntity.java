package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    @Column(name = "position")
    private int position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id")
    private AppUserEntity user;

    @OneToMany(mappedBy="course", fetch = FetchType.LAZY)
    private List<DayEntity> days;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tag-collection_id", unique = true)
    private TagCollectionEntity tagCollection;

}
