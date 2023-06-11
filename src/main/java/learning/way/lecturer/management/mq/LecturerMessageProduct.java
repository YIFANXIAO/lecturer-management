package learning.way.lecturer.management.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.way.lecturer.management.message.CoursePaymentRequestCreatedMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LecturerMessageProduct {

    private final ObjectMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    public boolean send(CoursePaymentRequestCreatedMessage coursePaymentRequestCreatedMessage) {
        try {
            String message = mapper.writeValueAsString(coursePaymentRequestCreatedMessage);
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUNTING_KEY, message);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
