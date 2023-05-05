package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class CourseSpecifications {

    public static Specification<CourseEntity> byUser(long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(CourseEntity_.USER).get(AppUserEntity_.ID), userId);
    }

    public static Specification<CourseEntity> fromUsers(Set<Long> usersIdCollection) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .in(root.get(CourseEntity_.USER).get(AppUserEntity_.ID))
                .value(usersIdCollection);
    }

    public static Specification<CourseEntity> byId(Set<Long> coursesId) {
        return (root, query, criteriaBuilder) -> {
            if (coursesId == null || coursesId.isEmpty()) {
                return null;
            }
            return criteriaBuilder
                    .in(root.get(CourseEntity_.ID))
                    .value(coursesId);
        };
    }
}
