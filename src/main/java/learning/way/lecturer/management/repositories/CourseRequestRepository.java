package learning.way.lecturer.management.repositories;

import learning.way.lecturer.management.entities.CourseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {

}