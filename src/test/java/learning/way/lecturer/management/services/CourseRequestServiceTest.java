package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
import learning.way.lecturer.management.repository.CourseRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseRequestServiceTest {

    @Test
    void should_save_course_request_and_submit_request_to_course_content_integration() {
        CourseContentClient courseContentClient = Mockito.mock(CourseContentClient.class);

        CourseRequestRepository courseRequestRepository = Mockito.mock(CourseRequestRepository.class);
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2023-07-01T00:00:00Z"))
            .build();
        CourseRequest request = CourseRequest.builder()
            .id(1L)
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2023-07-01T00:00:00Z"))
            .build();
        when(courseRequestRepository.saveAndFlush(any())).thenReturn(request);

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);
        Long courseRequestId = courseRequestService.submitCourseRequest(requestDto);

        assertEquals(1L, courseRequestId);
        verify(courseContentClient, times(1)).submitCourseRequest(requestDto);
    }

}