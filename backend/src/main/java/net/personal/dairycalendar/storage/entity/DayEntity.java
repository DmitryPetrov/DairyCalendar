package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "day",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "date"}))
public class DayEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id", nullable=false)
    private CourseEntity course;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "assessment")
    private int assessment;

}
