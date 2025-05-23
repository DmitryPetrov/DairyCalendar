package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.CourseEntity_;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.entity.DayEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class CourseDaySpecifications {

    public static Specification<DayEntity> inPeriod(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }
        final LocalDate endDate = Optional.ofNullable(toDate).orElse(LocalDate.now());
        final LocalDate startDate = Optional.ofNullable(fromDate).orElse(endDate.minusDays(7));
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(DayEntity_.DATE), startDate, endDate);
    }

    public static Specification<DayEntity> inDate(Set<LocalDate> dates) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .in(root.get(DayEntity_.DATE))
                .value(dates);
    }

    public static Specification<DayEntity> inCourse(Set<Long> courseIdCollection) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .in(root.get(DayEntity_.COURSE).get(CourseEntity_.ID))
                .value(courseIdCollection);
    }
}
