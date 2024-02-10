package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "course",
        uniqueConstraints = @UniqueConstraint(columnNames = {"app-user_id", "title"}))
@NoArgsConstructor
public class CourseEntity extends EntityWithTags{

    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "position")
    private int position;
    @Column(name = "paused")
    private boolean paused;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id", nullable = false)
    private AppUserEntity user;

    @OneToMany(mappedBy="course", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DayEntity> days = new HashSet<>();

    public CourseEntity(
            String title,
            String description,
            int position,
            AppUserEntity user,
            Collection<TagEntity> tags
    ) {
        super(tags);
        this.title = title;
        this.description = description;
        this.position = position;
        this.user = user;
    }

}
