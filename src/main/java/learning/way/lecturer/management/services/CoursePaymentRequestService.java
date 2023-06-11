package learning.way.lecturer.management.services;

import learning.way.lecturer.management.dtos.CoursePaymentRequestDto;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.entities.CoursePaymentRequest;
import learning.way.lecturer.management.enums.ErrorCode;
import learning.way.lecturer.management.exceptions.BaseBusinessException;
import learning.way.lecturer.management.message.CoursePaymentRequestCreatedMessage;
import learning.way.lecturer.management.mq.LecturerMessageProduct;
import learning.way.lecturer.management.repositories.CoursePaymentRequestRepository;
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
public class CoursePaymentRequestService {

    private final CoursePaymentRequestRepository coursePaymentRequestRepository;

    private final LecturerMessageProduct lecturerMessageProduct;

    public boolean validateCoursePaymentRequest(CoursePaymentRequestDto coursePaymentRequestDto) {
        return Stream.of(
                    !Objects.isNull(coursePaymentRequestDto.getCourseId()),
                    !Objects.isNull(coursePaymentRequestDto.getAmount()),
                    !Objects.isNull(coursePaymentRequestDto.getCreatedAt()),
                    !Objects.isNull(coursePaymentRequestDto.getExpiredAt()),
                    coursePaymentRequestDto.getExpiredAt().isAfter(Instant.now())
               ).allMatch(item -> item);
    }

    @Transactional
    public Long submitCoursePaymentRequest(CoursePaymentRequestDto coursePaymentRequestDto, Long contractId) {

        try {
            CoursePaymentRequestCreatedMessage coursePaymentRequestCreatedMessage = CoursePaymentRequestCreatedMessage.builder()
                .courseId(coursePaymentRequestDto.getCourseId())
                .amount(coursePaymentRequestDto.getAmount())
                .build();
            lecturerMessageProduct.send(coursePaymentRequestCreatedMessage);
        } catch (Exception e) {
            log.error("send message error: ", e);
            throw new BaseBusinessException(ErrorCode.UNKNOWN_ERROR);
        }

        CoursePaymentRequest coursePaymentRequest = CoursePaymentRequest.builder()
            .courseId(coursePaymentRequestDto.getCourseId())
            .amount(coursePaymentRequestDto.getAmount())
            .description(coursePaymentRequestDto.getDescription())
            .createdAt(coursePaymentRequestDto.getCreatedAt())
            .expiredAt(coursePaymentRequestDto.getExpiredAt())
            .build();
        coursePaymentRequest = coursePaymentRequestRepository.saveAndFlush(coursePaymentRequest);


        return coursePaymentRequest.getId();
    }

}
