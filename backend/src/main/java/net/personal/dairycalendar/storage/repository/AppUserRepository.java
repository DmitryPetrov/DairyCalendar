package net.personal.dairycalendar.storage.repository;

import net.personal.dairycalendar.storage.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
}
