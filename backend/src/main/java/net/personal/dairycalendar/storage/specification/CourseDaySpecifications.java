package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class CourseDaySpecifications {

    public static Specification<DayEntity> inPeriod(LocalDate fromDate, LocalDate toDate) {
        final LocalDate endDate = Optional.ofNullable(toDate).orElse(LocalDate.now());
        final LocalDate startDate = Optional.ofNullable(fromDate).orElse(endDate.minusDays(7));
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(DayEntity_.DATE), startDate, endDate);
    }

    public static Specification<DayEntity> hasTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags.isEmpty()) {
                return null;
            }
            return criteriaBuilder
                .in(root.get(DayEntity_.COURSE).get(CourseEntity_.TAGS).get(TagEntity_.TAG))
                .value(tags);
        };
    }
}
