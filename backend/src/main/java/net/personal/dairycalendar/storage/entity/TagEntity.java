package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
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
@Table(name = "tag")
@NoArgsConstructor
public class TagEntity extends BaseEntity {

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<TagCollectionEntity> courses;

    @Column(name = "tag", unique = true)
    private String tag;

    public TagEntity(String tag) {
        this.tag = tag;
    }
}
