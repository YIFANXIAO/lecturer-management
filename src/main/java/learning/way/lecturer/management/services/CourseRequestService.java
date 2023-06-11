package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.ErrorCode;
import learning.way.lecturer.management.exceptions.TimeOutException;
import learning.way.lecturer.management.repositories.CourseRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Long submitCourseRequest(CourseRequestDto courseRequestDto, Long contractId) {

        try {
            courseContentClient.submitCourseRequest(courseRequestDto);
        } catch (TimeOutException e) {
            log.error("invoke courseContentClient.submitCourseRequest timeOut", e);
            throw new TimeOutException(ErrorCode.INVOKE_TIMEOUT);
        }

        CourseRequest courseRequest = CourseRequest.builder()
            .name(courseRequestDto.getName())
            .type(courseRequestDto.getType())
            .description(courseRequestDto.getDescription())
            .contractId(contractId)
            .createdAt(courseRequestDto.getCreatedAt())
            .expiredAt(courseRequestDto.getExpiredAt())
            .build();
        courseRequest = courseRequestRepository.saveAndFlush(courseRequest);


        return courseRequest.getId();
    }

}
