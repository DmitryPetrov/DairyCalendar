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
import net.personal.dairycalendar.storage.entity.TagEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.repository.TagRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import net.personal.dairycalendar.storage.specification.CourseSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DayRepository dayRepository;
    private final TagRepository tagRepository;
    private final AppUserRepository appUserRepository;
    private final CourseMapper courseMapper;
    private final DayMapper dayMapper;
    private final AuthenticationService authenticationService;

    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesForCurrentUser(LocalDate fromDate, LocalDate toDate, Set<String> tags) {
        long currentUserId = authenticationService
                .getCurrentUser()
                .getId();
        List<CourseEntity> courses = courseRepository
                .findAll(Specification.where(CourseSpecifications.fromUser(currentUserId)));

        Set<Long> coursesIdList = courses
                .stream()
                .map(CourseEntity::getId)
                .collect(Collectors.toSet());
        Specification<DayEntity> daysByCoursesInPeriod = Specification
                .where(CourseDaySpecifications.inCourse(coursesIdList))
                .and(CourseDaySpecifications.inPeriod(fromDate, toDate.plusDays(1)));
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
        Set<TagEntity> existedTags = tagRepository.findAllByTagIn(courseDto.getTags());
        Set<TagEntity> newTags = courseDto
                .getTags()
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
        newTags.removeIf(newTag -> existedTags
                .stream()
                .map(TagEntity::getTag)
                .anyMatch(existedTag -> existedTag.equalsIgnoreCase(newTag.getTag())));
        course.addTags(existedTags);
        course.addTags(newTags);
        tagRepository.saveAll(newTags);
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
        CourseEntity entity = courseRepository
                .findById(id)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, id));

        entity.setTitle(courseDto.getTitle());
        entity.setDescription(courseDto.getDescription());
        entity.setPosition(courseDto.getPosition());

        Set<TagEntity> existedTags = tagRepository.findAllByTagIn(courseDto.getTags());
        Set<TagEntity> newTags = courseDto
                .getTags()
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
        newTags.removeIf(newTag -> existedTags
                .stream()
                .map(TagEntity::getTag)
                .anyMatch(existedTag -> existedTag.equalsIgnoreCase(newTag.getTag())));
        tagRepository.saveAll(newTags);

        Set<TagEntity> newTagCollection = new HashSet<>();
        newTagCollection.addAll(existedTags);
        newTagCollection.addAll(newTags);
        entity.updateTags(newTagCollection);

        courseRepository.save(entity);
    }

    public void deleteCourse(long id) {
        courseRepository.deleteAllById(Set.of(id));
    }
}
