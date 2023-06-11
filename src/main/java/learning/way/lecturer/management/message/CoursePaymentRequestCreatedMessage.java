package learning.way.lecturer.management.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePaymentRequestCreatedMessage {
    private Long courseId;
    private Long amount;
}

