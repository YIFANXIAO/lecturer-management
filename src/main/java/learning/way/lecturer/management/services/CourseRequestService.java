package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.repository.CourseRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRequestService {

    private final CourseRequestRepository courseRequestRepository;

    private final CourseContentClient courseContentClient;

    public Long submitCourseRequest(CourseRequestDto courseRequestDto) {

        CourseRequest courseRequest = CourseRequest.builder()
            .name(courseRequestDto.getName())
            .type(courseRequestDto.getType())
            .desc(courseRequestDto.getDesc())
            .createdAt(courseRequestDto.getCreatedAt())
            .expiredAt(courseRequestDto.getExpiredAt())
            .build();
        courseRequest = courseRequestRepository.saveAndFlush(courseRequest);

        courseContentClient.submitCourseRequest(courseRequestDto);

        return courseRequest.getId();
    }

}