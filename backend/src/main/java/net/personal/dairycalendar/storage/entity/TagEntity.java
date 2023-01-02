package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Tag")
@NoArgsConstructor
public class TagEntity extends BaseEntity {

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<CourseEntity> courses;

    private String tag;

    public TagEntity(String tag) {
        this.tag = tag;
    }
}
