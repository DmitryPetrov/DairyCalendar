package net.personal.dairycalendar.storage.repository;

import net.personal.dairycalendar.storage.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<DayEntity, Long>,
        JpaSpecificationExecutor<DayEntity> {

}
