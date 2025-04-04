package net.personal.dairycalendar.storage.repository;

import net.personal.dairycalendar.storage.entity.DayDescription;
import net.personal.dairycalendar.storage.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DayDescriptionRepository extends JpaRepository<DayDescription, Long>,
        JpaSpecificationExecutor<DayDescription> {

}
