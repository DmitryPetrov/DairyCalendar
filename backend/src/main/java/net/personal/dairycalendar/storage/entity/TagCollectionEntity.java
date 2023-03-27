package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tag-collection")
public class TagCollectionEntity extends BaseEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tag-collection_tag",
            joinColumns = @JoinColumn(name = "tag-collection_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    public void addTags(Collection<TagEntity> tags) {
        this.getTags().addAll(tags);
    }

}
