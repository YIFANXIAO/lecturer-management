package learning.way.lecturer.management.clients;

import learning.way.lecturer.management.dtos.CourseRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "course-content-integration", url = "${feign.course-content-integration.url}")
public interface CourseContentClient {

    @PostMapping(value = "/api/course-request")
    void submitCourseRequest(@RequestBody CourseRequestDto courseRequest);

}