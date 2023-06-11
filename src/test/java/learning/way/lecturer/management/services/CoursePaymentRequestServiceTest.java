package learning.way.lecturer.management.services;

import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CoursePaymentRequestDto;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CoursePaymentRequest;
import learning.way.lecturer.management.enums.CourseType;
import learning.way.lecturer.management.exceptions.BaseBusinessException;
import learning.way.lecturer.management.exceptions.TimeOutException;
import learning.way.lecturer.management.mq.LecturerMessageProduct;
import learning.way.lecturer.management.repositories.CoursePaymentRequestRepository;
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

class CoursePaymentRequestServiceTest {

    @Test
    void should_save_course_payment_request_and_submit_request_to_payment_system() {
        LecturerMessageProduct lecturerMessageProduct = Mockito.mock(LecturerMessageProduct.class);

        CoursePaymentRequestRepository coursePaymentRequestRepository = Mockito.mock(CoursePaymentRequestRepository.class);

        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        CoursePaymentRequest request = CoursePaymentRequest.builder()
            .id(1L)
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        when(coursePaymentRequestRepository.saveAndFlush(any())).thenReturn(request);

        CoursePaymentRequestService coursePaymentRequestService = new CoursePaymentRequestService(coursePaymentRequestRepository, lecturerMessageProduct);
        Long coursePaymentRequestId = coursePaymentRequestService.submitCoursePaymentRequest(requestDto, 1L);

        assertEquals(1L, coursePaymentRequestId);
        assertEquals(100L, requestDto.getAmount());
        verify(coursePaymentRequestRepository, times(1)).saveAndFlush(any());
        verify(lecturerMessageProduct, times(1)).send(any());
    }

    @Test
    void should_not_save_course_payment_request_when_send_message_failed() {
        LecturerMessageProduct lecturerMessageProduct = Mockito.mock(LecturerMessageProduct.class);

        CoursePaymentRequestRepository coursePaymentRequestRepository = Mockito.mock(CoursePaymentRequestRepository.class);

        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        CoursePaymentRequest request = CoursePaymentRequest.builder()
            .id(1L)
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        when(coursePaymentRequestRepository.saveAndFlush(any())).thenReturn(request);

        doThrow(BaseBusinessException.class).when(lecturerMessageProduct).send(any());

        CoursePaymentRequestService coursePaymentRequestService = new CoursePaymentRequestService(coursePaymentRequestRepository, lecturerMessageProduct);

        assertThrows(BaseBusinessException.class,
            () -> coursePaymentRequestService.submitCoursePaymentRequest(requestDto, 1L));

        verify(coursePaymentRequestRepository, times(0)).saveAndFlush(any());
        verify(lecturerMessageProduct, times(1)).send(any());
    }

    @Test
    void should_validate_course_payment_request_success_when_all_params_legal() {
        LecturerMessageProduct lecturerMessageProduct = Mockito.mock(LecturerMessageProduct.class);

        CoursePaymentRequestRepository coursePaymentRequestRepository = Mockito.mock(CoursePaymentRequestRepository.class);
        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        CoursePaymentRequestService coursePaymentRequestService = new CoursePaymentRequestService(coursePaymentRequestRepository, lecturerMessageProduct);
        boolean validateResult = coursePaymentRequestService.validateCoursePaymentRequest(requestDto);

        assertTrue(validateResult);
    }

    @Test
    void should_validate_course_payment_request_field_when_lack_amount() {
        LecturerMessageProduct lecturerMessageProduct = Mockito.mock(LecturerMessageProduct.class);

        CoursePaymentRequestRepository coursePaymentRequestRepository = Mockito.mock(CoursePaymentRequestRepository.class);
        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(null)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        CoursePaymentRequestService coursePaymentRequestService = new CoursePaymentRequestService(coursePaymentRequestRepository, lecturerMessageProduct);
        boolean validateResult = coursePaymentRequestService.validateCoursePaymentRequest(requestDto);

        assertFalse(validateResult);
    }

    @Test
    void should_validate_course_payment_request_field_when_expired_at_illegal() {
        LecturerMessageProduct lecturerMessageProduct = Mockito.mock(LecturerMessageProduct.class);

        CoursePaymentRequestRepository coursePaymentRequestRepository = Mockito.mock(CoursePaymentRequestRepository.class);
        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(null)
            .description(null)
            .createdAt(Instant.parse("1970-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("1970-07-01T00:00:00Z"))
            .build();

        CoursePaymentRequestService coursePaymentRequestService = new CoursePaymentRequestService(coursePaymentRequestRepository, lecturerMessageProduct);
        boolean validateResult = coursePaymentRequestService.validateCoursePaymentRequest(requestDto);

        assertFalse(validateResult);
    }
    
}