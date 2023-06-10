package learning.way.lecturer.management.repository;

import learning.way.lecturer.management.entities.CourseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {
}