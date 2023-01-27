package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.mapper.CourseMapper;
import net.personal.dairycalendar.dto.mapper.DayMapper;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import net.personal.dairycalendar.storage.specification.CourseSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthenticationService authenticationService;

    @Transactional
    public List<CourseDto> getCoursesForCurrentUser(LocalDate fromDate, LocalDate toDate, Set<String> tags) {
        Specification<CourseEntity> courseSpecification = Specification
                .where(CourseSpecifications.fromUsers(Set.of(authenticationService.getCurrentUser().getId())));

        List<CourseEntity> courses = courseRepository.findAll(courseSpecification);

        Specification<DayEntity> specification = Specification
                .where(CourseDaySpecifications.inCourse(
                        courses.stream().map(CourseEntity::getId).collect(Collectors.toSet())
                       ))
                .and(CourseDaySpecifications.inPeriod(fromDate, toDate.plusDays(1)));

        List<DayEntity> days = dayRepository.findAll(specification);

        return courses
                .stream()
                .map(entity -> {
                    CourseDto courseDto = courseMapper.toDto(entity);
                    //courseDto.setTags(entity.getTagCollection().getTags().stream().map(TagEntity::getTag).collect(Collectors.toSet()));
                    courseDto.setDays(
                            days
                                    .stream()
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
                    //courseDto.setTags(entity.getTagCollection().getTags().stream().map(TagEntity::getTag).collect(Collectors.toSet()));
                    return courseDto;
                })
                .collect(Collectors.toList());
    }
}
