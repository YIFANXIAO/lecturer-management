package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.repositories.CourseRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRequestService {

    private final CourseRequestRepository courseRequestRepository;

    private final CourseContentClient courseContentClient;

    public boolean validateCourseRequest(CourseRequestDto courseRequestDto) {
        return Stream.of(
                    StringUtils.hasText(courseRequestDto.getName()),
                    !Objects.isNull(courseRequestDto.getType()),
                    !Objects.isNull(courseRequestDto.getContractId()),
                    !Objects.isNull(courseRequestDto.getCreatedAt()),
                    !Objects.isNull(courseRequestDto.getExpiredAt()),
                    courseRequestDto.getExpiredAt().isAfter(Instant.now())
               ).allMatch(item -> item);
    }

    public Long submitCourseRequest(CourseRequestDto courseRequestDto, Long contractId) {

        CourseRequest courseRequest = CourseRequest.builder()
            .name(courseRequestDto.getName())
            .type(courseRequestDto.getType())
            .description(courseRequestDto.getDescription())
            .contractId(contractId)
            .createdAt(courseRequestDto.getCreatedAt())
            .expiredAt(courseRequestDto.getExpiredAt())
            .build();
        courseRequest = courseRequestRepository.saveAndFlush(courseRequest);

        courseContentClient.submitCourseRequest(courseRequestDto);

        return courseRequest.getId();
    }

    public CourseRequestDto getCourseRequest(Long contractId, Long courseRequestId) {

        CourseRequest courseRequest = courseRequestRepository.findByContractIdAndId(contractId, courseRequestId);

        return CourseRequestDto.builder()
            .name(courseRequest.getName())
            .type(courseRequest.getType())
            .description(courseRequest.getDescription())
            .contractId(courseRequest.getContractId())
            .createdAt(courseRequest.getCreatedAt())
            .expiredAt(courseRequest.getExpiredAt())
            .build();
    }

}
