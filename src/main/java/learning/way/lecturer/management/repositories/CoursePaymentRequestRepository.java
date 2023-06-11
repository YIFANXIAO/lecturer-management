package learning.way.lecturer.management.repositories;

import learning.way.lecturer.management.entities.CoursePaymentRequest;
import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoursePaymentRequestRepository extends JpaRepository<CoursePaymentRequest, Long> {

}