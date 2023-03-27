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
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "course",
        uniqueConstraints = @UniqueConstraint(columnNames = {"app-user_id", "title"}))
@NoArgsConstructor
public class CourseEntity extends BaseEntity{

    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "position")
    private int position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id", nullable = false)
    private AppUserEntity user;

    @OneToMany(mappedBy="course", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DayEntity> days = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tag-collection_id", unique = true)
    private TagCollectionEntity tagCollection = new TagCollectionEntity();

    public CourseEntity(
            String title,
            String description,
            int position,
            AppUserEntity user,
            Collection<TagEntity> tags
    ) {
        this.title = title;
        this.description = description;
        this.position = position;
        this.user = user;
        this.addTags(tags);
    }

    public CourseEntity addTags(Collection<TagEntity> tags) {
        if (this.getTagCollection() == null) {
            this.setTagCollection(new TagCollectionEntity());
        }
        this.getTagCollection().addTags(tags);
        return this;
    }

    public Set<String> getTags() {
        return Optional.ofNullable(this.getTagCollection())
                .map(TagCollectionEntity::getTags)
                .stream()
                .flatMap(Collection::stream)
                .map(TagEntity::getTag)
                .collect(Collectors.toSet());
    }

    public void updateTags(Set<TagEntity> tags) {
        Set<TagEntity> newTags = new HashSet<>(tags);

        if (this.getTagCollection() == null) {
            this.setTagCollection(new TagCollectionEntity());
        }

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
}
