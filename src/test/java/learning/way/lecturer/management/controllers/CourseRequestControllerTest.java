package learning.way.lecturer.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.way.lecturer.management.bases.TestBase;
import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CoursePaymentRequestDto;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CoursePaymentRequest;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
import learning.way.lecturer.management.exceptions.BaseBusinessException;
import learning.way.lecturer.management.exceptions.TimeOutException;
import learning.way.lecturer.management.mq.LecturerMessageProduct;
import learning.way.lecturer.management.repositories.CoursePaymentRequestRepository;
import learning.way.lecturer.management.repositories.CourseRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CourseRequestControllerTest extends TestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Resource
    CourseRequestRepository courseRequestRepository;

    @Resource
    CoursePaymentRequestRepository coursePaymentRequestRepository;

    @MockBean
    CourseContentClient courseContentClient;

    @MockBean
    LecturerMessageProduct lecturerMessageProduct;

    @Test
    void should_submit_course_request_success_when_params_legal() throws Exception {

        long contractId = 1L;
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/contracts/" + contractId + "/courses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(1)));

        CourseRequest courseRequest = courseRequestRepository.findByContractId(1L);

        assertEquals("LinearAlgebra", courseRequest.getName());
        assertEquals(CourseType.HIGHER_MATHEMATICS, courseRequest.getType());
        assertEquals(Instant.parse("2023-06-01T00:00:00Z"), courseRequest.getCreatedAt());
        assertEquals(Instant.parse("2099-07-01T00:00:00Z"), courseRequest.getExpiredAt());

    }

    @Test
    void should_not_save_request_when_course_content_system_timeout() throws Exception {

        long contractId = 1L;
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        doThrow(TimeOutException.class).when(courseContentClient).submitCourseRequest(any());

        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/contracts/" + contractId + "/courses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        CourseRequest courseRequest = courseRequestRepository.findByContractId(1L);

        assertNull(courseRequest);
    }

    @Test
    void should_validate_course_request_failed_when_lack_type() throws Exception {

        long contractId = 1L;
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/contracts/" + contractId + "/courses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_course_request_failed_when_expired_at_illegal() throws Exception {

        long contractId = 1L;
        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("1970-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("1970-07-01T00:00:00Z"))
            .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/contracts/" + contractId + "/courses")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_submit_course_payment_request_success_when_params_legal() throws Exception {

        long contractId = 1L;
        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/contracts/" + contractId + "/course-payments")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(1)));

        CoursePaymentRequest coursePaymentRequest = coursePaymentRequestRepository.findByCourseId(1L);

        assertEquals(100, coursePaymentRequest.getAmount());
        assertEquals(1, coursePaymentRequest.getCourseId());
        assertEquals(Instant.parse("2023-06-01T00:00:00Z"), coursePaymentRequest.getCreatedAt());
        assertEquals(Instant.parse("2099-07-01T00:00:00Z"), coursePaymentRequest.getExpiredAt());

    }

    @Test
    void should_not_save_course_payment_request_when_send_message_failed() throws Exception {

        long contractId = 1L;
        CoursePaymentRequestDto requestDto = CoursePaymentRequestDto.builder()
            .courseId(1L)
            .amount(100L)
            .description(null)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        doThrow(BaseBusinessException.class).when(lecturerMessageProduct).send(any());

        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/contracts/" + contractId + "/course-payments")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        CoursePaymentRequest coursePaymentRequest = coursePaymentRequestRepository.findByCourseId(1L);

        assertNull(coursePaymentRequest);
    }

}