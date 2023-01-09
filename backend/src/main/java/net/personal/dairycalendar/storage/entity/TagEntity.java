package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tag")
@NoArgsConstructor
public class TagEntity extends BaseEntity {

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<CourseEntity> courses;

    @Column(name = "tag")
    private String tag;

    public TagEntity(String tag) {
        this.tag = tag;
    }
}
