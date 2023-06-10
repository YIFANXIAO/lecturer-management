package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.repositories.CourseRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRequestService {

    private final CourseRequestRepository courseRequestRepository;

    private final CourseContentClient courseContentClient;

    public Long submitCourseRequest(CourseRequestDto courseRequestDto, Long contractId) {

        CourseRequest courseRequest = CourseRequest.builder()
            .name(courseRequestDto.getName())
            .type(courseRequestDto.getType())
            .desc(courseRequestDto.getDesc())
            .contractId(contractId)
            .createdAt(courseRequestDto.getCreatedAt())
            .expiredAt(courseRequestDto.getExpiredAt())
            .build();
        courseRequest = courseRequestRepository.saveAndFlush(courseRequest);

        courseContentClient.submitCourseRequest(courseRequestDto);

        return courseRequest.getId();
    }

    public CourseRequestDto getCourseRequest(Long courseRequestId) {

        CourseRequest courseRequest = courseRequestRepository.findById(courseRequestId)
            .orElse(CourseRequest.builder().build());

        return CourseRequestDto.builder()
            .name(courseRequest.getName())
            .type(courseRequest.getType())
            .desc(courseRequest.getDesc())
            .contractId(courseRequest.getContractId())
            .createdAt(courseRequest.getCreatedAt())
            .expiredAt(courseRequest.getExpiredAt())
            .build();
    }

}
