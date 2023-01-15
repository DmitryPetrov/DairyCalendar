package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role")
public class RoleEntity  extends BaseEntity {

    @Column(name = "title", length = 255)
    private String title;

}
