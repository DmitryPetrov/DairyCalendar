package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.mapper.CourseMapper;
import net.personal.dairycalendar.dto.mapper.DayMapper;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.TagEntity;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DayRepository dayRepository;
    private final CourseMapper courseMapper;
    private final DayMapper dayMapper;

    public List<CourseDto> getCoursesForCurrentUser(LocalDate fromDate, LocalDate toDate, Set<String> tags) {
        Specification<DayEntity> specification = Specification
                .where(CourseDaySpecifications.hasTags(tags))
                .and(CourseDaySpecifications.inPeriod(fromDate, toDate));

        List<DayEntity> all = dayRepository.findAll(specification);

        Set<CourseEntity> courses = all.stream()
                .map(DayEntity::getCourse)
                .collect(Collectors.toSet());

        return courses
                .stream()
                .map(entity -> {
                    CourseDto courseDto = courseMapper.toDto(entity);
                    courseDto.setTags(entity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toSet()));
                    courseDto.setDays(
                        all.stream()
                                .filter(day -> day.getCourse().getId() == entity.getId())
                                .map(dayMapper::toDto)
                                .collect(Collectors.toSet())
                    );
                    return courseDto;
                })
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesForCurrentUser() {
        return courseRepository.findAll()
                .stream()
                .map(entity -> {
                    CourseDto courseDto = courseMapper.toDto(entity);
                    courseDto.setTags(entity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toSet()));
                    return courseDto;
                })
                .collect(Collectors.toList());
    }
}
