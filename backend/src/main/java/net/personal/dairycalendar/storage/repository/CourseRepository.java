package net.personal.dairycalendar.storage.repository;

import net.personal.dairycalendar.storage.entity.CourseEntity;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {


}
