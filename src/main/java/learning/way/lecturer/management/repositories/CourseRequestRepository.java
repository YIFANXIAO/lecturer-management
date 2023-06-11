package learning.way.lecturer.management.repositories;

import learning.way.lecturer.management.entities.CourseRequest;
import learning.way.lecturer.management.enums.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {

    CourseRequest findByContractIdAndId(Long contractId, Long id);

    Optional<CourseRequest> findByNameAndType(String name, CourseType type);

}