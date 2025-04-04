package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.DayDescription;
import net.personal.dairycalendar.storage.entity.DayDescription_;
import net.personal.dairycalendar.storage.entity.DayEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class DayDescriptionSpecifications {

    public static Specification<DayDescription> byUser(long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(DayDescription_.USER).get(AppUserEntity_.ID), userId);
    }

    public static Specification<DayDescription> inPeriod(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }
        final LocalDate endDate = Optional.ofNullable(toDate)
                .orElse(LocalDate.now());
        final LocalDate startDate = Optional.ofNullable(fromDate)
                .orElse(endDate.minusDays(7));
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(DayEntity_.DATE), startDate, endDate);
    }

    public static Specification<DayDescription> inDates(Set<LocalDate> dates) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .in(root.get(DayEntity_.DATE))
                .value(dates);
    }

}
