package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
import learning.way.lecturer.management.exceptions.TimeOutException;
import learning.way.lecturer.management.repositories.CourseRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        CourseRequest request = CourseRequest.builder()
            .id(1L)
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        when(courseRequestRepository.saveAndFlush(any())).thenReturn(request);

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);
        Long courseRequestId = courseRequestService.submitCourseRequest(requestDto, 1L);

        assertEquals(1L, courseRequestId);
        assertEquals("LinearAlgebra", requestDto.getName());
        assertEquals(CourseType.HIGHER_MATHEMATICS, requestDto.getType());
        verify(courseRequestRepository, times(1)).saveAndFlush(any());
        verify(courseContentClient, times(1)).submitCourseRequest(requestDto);
    }

    @Test
    void should_retry_invoke_course_content_system_and_not_save_request_when_timeout() {
        CourseContentClient courseContentClient = Mockito.mock(CourseContentClient.class);

        CourseRequestRepository courseRequestRepository = Mockito.mock(CourseRequestRepository.class);
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        CourseRequest request = CourseRequest.builder()
            .id(1L)
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        when(courseRequestRepository.saveAndFlush(any())).thenReturn(request);
        doThrow(TimeOutException.class).when(courseContentClient).submitCourseRequest(any());

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);

        assertThrows(TimeOutException.class,
            () -> courseRequestService.submitCourseRequest(requestDto, 1L));

        verify(courseContentClient, times(1)).submitCourseRequest(requestDto);
        verify(courseRequestRepository, times(0)).saveAndFlush(any());
    }

    @Test
    void should_validate_course_request_success_when_all_params_legal() {
        CourseContentClient courseContentClient = Mockito.mock(CourseContentClient.class);

        CourseRequestRepository courseRequestRepository = Mockito.mock(CourseRequestRepository.class);
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);
        boolean validateResult = courseRequestService.validateCourseRequest(requestDto);

        assertTrue(validateResult);
    }

    @Test
    void should_validate_course_request_field_when_lack_type() {
        CourseContentClient courseContentClient = Mockito.mock(CourseContentClient.class);

        CourseRequestRepository courseRequestRepository = Mockito.mock(CourseRequestRepository.class);
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);
        boolean validateResult = courseRequestService.validateCourseRequest(requestDto);

        assertFalse(validateResult);
    }

    @Test
    void should_validate_course_request_field_when_expired_at_illegal() {
        CourseContentClient courseContentClient = Mockito.mock(CourseContentClient.class);

        CourseRequestRepository courseRequestRepository = Mockito.mock(CourseRequestRepository.class);
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("1970-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("1970-07-01T00:00:00Z"))
            .build();

        CourseRequestService courseRequestService = new CourseRequestService(courseRequestRepository, courseContentClient);
        boolean validateResult = courseRequestService.validateCourseRequest(requestDto);

        assertFalse(validateResult);
    }

}