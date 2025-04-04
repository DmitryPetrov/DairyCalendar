package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "day-description",
        uniqueConstraints = @UniqueConstraint(columnNames = {"app-user_id", "date"}))
@AllArgsConstructor
@NoArgsConstructor
public class DayDescription extends EntityWithTags{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app-user_id", nullable = false)
    private AppUserEntity user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description", length = 2000)
    private String description;

}
