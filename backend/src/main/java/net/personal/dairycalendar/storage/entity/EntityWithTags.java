package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class EntityWithTags extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tag-collection_id", unique = true)
    private TagCollectionEntity tagCollection;

    public EntityWithTags(Collection<TagEntity> tags) {
        this.addTags(tags);
    }

    public TagCollectionEntity getTagCollection() {
        if (this.tagCollection == null) {
            this.tagCollection = new TagCollectionEntity();
        }
        return tagCollection;
    }

    public EntityWithTags addTags(Collection<TagEntity> tags) {
        this.getTagCollection().addTags(tags);
        return this;
    }

    public void updateTags(Set<TagEntity> tags) {
        Set<TagEntity> newTags = new HashSet<>(tags);

        Set<TagEntity> forDelete = new HashSet<>();
        for (TagEntity entity: this.getTagCollection().getTags()) {
            if (newTags.contains(entity)) {
                newTags.remove(entity);
            } else {
                forDelete.add(entity);
            }
        }

        this.getTagCollection().getTags().removeAll(forDelete);
        this.addTags(newTags);
    }

    public Set<String> getTags() {
        return this.getTagCollection()
                .getTags()
                .stream()
                .map(TagEntity::getTag)
                .collect(Collectors.toSet());
    }

}
