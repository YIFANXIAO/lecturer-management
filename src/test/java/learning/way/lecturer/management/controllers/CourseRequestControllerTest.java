package learning.way.lecturer.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.way.lecturer.management.bases.TestBase;
import learning.way.lecturer.management.clients.CourseContentClient;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
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

    @MockBean
    CourseContentClient courseContentClient;

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

        CourseRequest courseRequest = courseRequestRepository.findById(1L).orElse(CourseRequest.builder().build());

        assertEquals("LinearAlgebra", courseRequest.getName());
        assertEquals(CourseType.HIGHER_MATHEMATICS, courseRequest.getType());
        assertEquals(Instant.parse("2023-06-01T00:00:00Z"), courseRequest.getCreatedAt());
        assertEquals(Instant.parse("2099-07-01T00:00:00Z"), courseRequest.getExpiredAt());

    }

//    @Test
//    void should_validate_course_request_failed_when_lack_type() throws Exception {
//
//        long contractId = 1L;
//        CourseRequestDto requestDto = CourseRequestDto.builder()
//            .name("LinearAlgebra")
//            .contractId(1L)
//            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
//            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
//            .build();
//
//        String requestJson = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(post("/contracts/" + contractId + "/courses")
//                .content(requestJson)
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isBadRequest());
//    }

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
    void should_get_course_request_success_by_contract_id_and_course_id() throws Exception {

        long contractId = 1L;
        CourseRequest request = CourseRequest.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(contractId)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();
        request = courseRequestRepository.saveAndFlush(request);

        mockMvc.perform(get("/contracts/" + contractId + "/courses/" + request.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.contractId", is(1)))
            .andExpect(jsonPath("$.name", is("LinearAlgebra")))
            .andExpect(jsonPath("$.type", is(CourseType.HIGHER_MATHEMATICS.getCode())))
            .andExpect(jsonPath("$.createdAt", is("2023-06-01T00:00:00Z")))
            .andExpect(jsonPath("$.expiredAt", is("2099-07-01T00:00:00Z")));
    }

}