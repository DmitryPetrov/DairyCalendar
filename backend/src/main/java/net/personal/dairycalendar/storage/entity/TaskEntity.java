package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "task")
@NoArgsConstructor
public class TaskEntity extends EntityWithTags {

    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "description", length = 10000)
    private String description;
    @Column(name = "position")
    private int position;
    @Column(name = "priority")
    private int priority;
    @Column(name = "done")
    private boolean done;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id", nullable = false)
    private AppUserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskEntity parent;

}
