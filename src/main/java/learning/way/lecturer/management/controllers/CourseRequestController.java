package learning.way.lecturer.management.controllers;

import io.swagger.annotations.Api;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.services.CourseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "课程相关API")
@RequestMapping("/contracts/{cid}")
@RequiredArgsConstructor
public class CourseRequestController {

    private final CourseRequestService courseRequestService;

    @PostMapping("/courses")
    public Long submitCourseRequest(@PathVariable Long cid, @RequestBody CourseRequestDto courseRequestDto) {
        return courseRequestService.submitCourseRequest(courseRequestDto, cid);
    }

    @GetMapping("/courses/{coid}")
    public CourseRequestDto getCourseRequest(@PathVariable Long cid, @PathVariable Long coid) {
        return courseRequestService.getCourseRequest(cid, coid);
    }
}