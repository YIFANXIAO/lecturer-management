package learning.way.lecturer.management.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.way.lecturer.management.message.CoursePaymentRequestCreatedMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class lecturerMessageProduceTest {

    @Test
    void should_return_true_given_send_message_success() {

        RabbitTemplate stubTabbitTemplate = Mockito.mock(RabbitTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        LecturerMessageProduct lecturerMessageProduct = new LecturerMessageProduct(objectMapper, stubTabbitTemplate);
        boolean sendMessageResult = lecturerMessageProduct.send(CoursePaymentRequestCreatedMessage.builder()
                                                                                .courseId(1L)
                                                                                .amount(100L)
                                                                                .build());
        assertTrue(sendMessageResult);
    }

    @Test
    void should_return_false_given_send_message_fail() {

        RabbitTemplate stubTabbitTemplate = Mockito.mock(RabbitTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.doThrow(AmqpException.class).when(stubTabbitTemplate).convertAndSend(anyString(), anyString(), java.util.Optional.ofNullable(any()));
        LecturerMessageProduct ticketMessageSender = new LecturerMessageProduct(objectMapper, stubTabbitTemplate);
        boolean sendMessageResult = ticketMessageSender.send(CoursePaymentRequestCreatedMessage.builder()
                                                                                .courseId(1L)
                                                                                .amount(100L)
                                                                                .build());
        assertFalse(sendMessageResult);
    }
}