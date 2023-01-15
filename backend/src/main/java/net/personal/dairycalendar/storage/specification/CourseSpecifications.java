package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity_;
import net.personal.dairycalendar.storage.entity.DayEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CourseSpecifications {

    public static Specification<CourseEntity> inPeriod(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get(CourseEntity_.DAYS).get(DayEntity_.DATE), fromDate, toDate);
        };
    }

/*    public static Specification<CourseEntity> hasTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .in(root.get(CourseEntity_.TAGS).get(TagEntity_.TAG))
                    .value(tags);
        };
    }*/
}
