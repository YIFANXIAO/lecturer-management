package learning.way.lecturer.management.dtos;

import learning.way.lecturer.management.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {

    private String name;

    private CourseType type;

    private String desc;

    private Long contractId;

    private Instant createdAt;

    private Instant expiredAt;

}
