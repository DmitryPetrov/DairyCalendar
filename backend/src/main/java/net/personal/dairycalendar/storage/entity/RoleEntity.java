package net.personal.dairycalendar.storage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.personal.dairycalendar.service.Role;

@Getter
@Setter
@Entity
@Table(name = "role")
public class RoleEntity  extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "title", length = 255)
    private Role title;

}
