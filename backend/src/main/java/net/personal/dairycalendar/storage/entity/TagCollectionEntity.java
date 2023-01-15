package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tag-collection")
public class TagCollectionEntity extends BaseEntity{

    @ManyToMany
    @JoinTable(name = "tag-collection_tag",
            joinColumns = @JoinColumn(name = "tag-collection_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags = new LinkedHashSet<>();

}
