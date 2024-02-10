package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.mapper.CourseMapper;
import net.personal.dairycalendar.dto.mapper.DayMapper;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import net.personal.dairycalendar.storage.specification.CourseSpecifications;
import net.personal.dairycalendar.storage.specification.EntityWithTagsSpecifications;
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
    private final AppUserRepository appUserRepository;
    private final CourseMapper courseMapper;
    private final DayMapper dayMapper;
    private final AuthenticationService authenticationService;
    private final TagService tagService;

    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesForCurrentUser(
            LocalDate fromDate,
            LocalDate toDate,
            Set<String> tags,
            Set<Long> coursesId,
            boolean paused
    ) {
        Specification<CourseEntity> specification = Specification
                .where(CourseSpecifications.byUser(authenticationService.getCurrentUser().getId()))
                .and(CourseSpecifications.byId(coursesId))
                .and(CourseSpecifications.isPaused(paused))
                .and(EntityWithTagsSpecifications.hasTags(tags, CourseEntity.class));
        List<CourseEntity> courses = courseRepository.findAll(specification);
        Set<Long> coursesIdList = courses
                .stream()
                .map(CourseEntity::getId)
                .collect(Collectors.toSet());
        Specification<DayEntity> daysByCoursesInPeriod = Specification
                .where(CourseDaySpecifications.inCourse(coursesIdList))
                .and(CourseDaySpecifications.inPeriod(fromDate, toDate));
        List<DayEntity> days = dayRepository.findAll(daysByCoursesInPeriod);

        return courses
                .stream()
                .map(entity -> {
                    CourseDto courseDto = courseMapper.toDto(entity);
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

    @Transactional
    public long addCourse(CourseDto courseDto) {
        CourseEntity course = courseMapper.toEntity(courseDto);
        tagService.updateTagCollection(course, courseDto.getTags());
        long currentUserId = authenticationService.getCurrentUser().getId();
        course.setUser(appUserRepository
                               .findById(currentUserId)
                               .orElseThrow(() -> new RecordIsNotExistException(AppUserEntity.class, currentUserId)));
        return courseRepository
                .save(course)
                .getId();
    }

    public CourseDto getCourse(long id) {
        return courseRepository
                .findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, id));
    }

    @Transactional
    public void updateCourse(long id, CourseDto courseDto) {
        CourseEntity course = courseRepository
                .findById(id)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, id));

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setPosition(courseDto.getPosition());
        course.setPaused(courseDto.isPaused());
        tagService.updateTagCollection(course, courseDto.getTags());
        courseRepository.save(course);
    }

    public void deleteCourse(long id) {
        courseRepository.deleteAllById(Set.of(id));
    }
}
