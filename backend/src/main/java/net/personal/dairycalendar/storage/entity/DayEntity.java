package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "course-step")
public class DayEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id", nullable=false)
    private CourseEntity course;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "assessment")
    private int assessment;

}
