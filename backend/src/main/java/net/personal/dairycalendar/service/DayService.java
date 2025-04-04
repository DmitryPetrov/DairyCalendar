package net.personal.dairycalendar.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayDescriptionDto;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.dto.DayInformationDto;
import net.personal.dairycalendar.dto.mapper.DayMapper;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayDescription;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayDescriptionRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import net.personal.dairycalendar.storage.specification.DayDescriptionSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayService {

    private final DayMapper dayMapper;
    private final CourseRepository courseRepository;
    private final DayRepository dayRepository;
    private final DayDescriptionRepository dayDescriptionRepository;
    private final AuthenticationService authenticationService;

    //todo проверять юзера перед сохранением
    @Transactional
    public void saveActivities(List<DayDto> dayCollection) {
        log.debug("Add new days for user [{}]", authenticationService.getCurrentUser());
        Set<Long> coursesId = dayCollection
                .stream()
                .map(DayDto::getCourseId)
                .collect(Collectors.toSet());
        List<CourseEntity> courseEntities = courseRepository.findAllById(coursesId);

        log.debug("Update saved course days");
        Set<Long> courseIdCollection = dayCollection
                .stream()
                .map(DayDto::getCourseId)
                .collect(Collectors.toSet());
        Set<LocalDate> dateCollection = dayCollection
                .stream()
                .map(DayDto::getDate)
                .collect(Collectors.toSet());
        Specification<DayEntity> specification = Specification
                .where(CourseDaySpecifications.inCourse(courseIdCollection))
                .and(CourseDaySpecifications.inDate(dateCollection));
        List<DayEntity> savedDays = dayRepository.findAll(specification);
        List<DayDto> editableDayCollection = new ArrayList<>(dayCollection);
        List<DayEntity> updatedDays = savedDays
                .stream()
                .peek(entity -> {
                    DayDto found = null;
                    for (DayDto dto : editableDayCollection) {
                        if ((dto.getCourseId() == entity.getCourse().getId()) &&
                                (dto.getDate().equals(entity.getDate()))) {
                            found = dto;
                            editableDayCollection.remove(found);
                            break;
                        }
                    }
                    entity.setAssessment(found.getAssessment());
                })
                .collect(Collectors.toList());
        dayRepository.saveAll(updatedDays);

        log.debug("Save new course days");
        List<DayEntity> days = editableDayCollection
                .stream()
                .map(dayDto -> {
                    Optional<CourseEntity> course = courseEntities
                            .stream()
                            .filter(entity -> (entity.getId() == dayDto.getCourseId()))
                            .findFirst();
                    if (course.isEmpty()) {
                        return null;
                    }
                    DayEntity dayEntity = dayMapper.toEntity(dayDto);
                    dayEntity.setCourse(course.get());
                    return dayEntity;
                })
                .filter(Objects::nonNull)
                .toList();
        dayRepository.saveAll(days);
    }

    @Transactional
    public void save(DayInformationDto dayCollection) {
        log.debug("save_day_info: save days info for user [{}]", authenticationService.getCurrentUser());
        saveActivities(dayCollection.getActivities());
        saveDescription(dayCollection.getDescriptions());

    }

    @Transactional
    public void saveDescription(List<DayDescriptionDto> descriptions) {
        AppUser currentUser = authenticationService.getCurrentUser();
        log.debug("Add new days descriptions for user [{}]", currentUser);

        Set<LocalDate> dateCollection = descriptions
                .stream()
                .map(DayDescriptionDto::getDate)
                .collect(Collectors.toSet());
        Specification<DayDescription> specification = Specification
                .where(DayDescriptionSpecifications.byUser(currentUser.getId()))
                .and(DayDescriptionSpecifications.inDates(dateCollection));
        List<DayDescription> savedDescriptions = dayDescriptionRepository.findAll(specification);
        List<DayDescriptionDto> editableDayCollection = new ArrayList<>(descriptions);
        List<DayDescription> updatedDescriptions = savedDescriptions
                .stream()
                .peek(entity -> {
                    DayDescriptionDto found = null;
                    for (DayDescriptionDto dto : editableDayCollection) {
                        if (dto.getDate().equals(entity.getDate())) {
                            found = dto;
                            editableDayCollection.remove(found);
                            break;
                        }
                    }
                    entity.setDescription(found.getDescription());
                })
                .collect(Collectors.toList());
        dayDescriptionRepository.saveAll(updatedDescriptions);

        final AppUserEntity currentUserEntity = authenticationService.getCurrentUserEntity();
        List<DayDescription> days = editableDayCollection
                .stream()
                .map(dto -> {
                    if (!StringUtils.hasLength(dto.getDescription())) {
                        return null;
                    }
                    DayDescription dayDesc = new DayDescription();
                    dayDesc.setDescription(dto.getDescription());
                    dayDesc.setDate(dto.getDate());
                    dayDesc.setUser(currentUserEntity);
                    return dayDesc;
                })
                .filter(Objects::nonNull)
                .toList();
        dayDescriptionRepository.saveAll(days);
    }

    public List<DayDescriptionDto> getDaysDescriptions(LocalDate from, LocalDate to) {
        Specification<DayDescription> specification = Specification
                .where(DayDescriptionSpecifications.byUser(authenticationService.getCurrentUser().getId()))
                .and(DayDescriptionSpecifications.inPeriod(from, to));
        List<DayDescriptionDto> descriptions = dayDescriptionRepository.findAll(specification)
                .stream()
                .map(entity -> {
                    if (!StringUtils.hasLength(entity.getDescription())) {
                        return null;
                    }
                    return new DayDescriptionDto(entity.getDate(), entity.getDescription());
                })
                .filter(Objects::nonNull)
                .toList();
        return descriptions;
    }
}
