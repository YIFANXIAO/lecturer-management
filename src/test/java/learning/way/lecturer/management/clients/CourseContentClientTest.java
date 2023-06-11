package learning.way.lecturer.management.clients;

import learning.way.lecturer.management.bases.TestBase;
import learning.way.lecturer.management.dtos.CourseRequestDto;
import learning.way.lecturer.management.enums.CourseType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.model.NottableString;
import org.mockserver.verify.VerificationTimes;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class CourseContentClientTest extends TestBase {

    @Resource
    CourseContentClient courseContentClient;

    private static MockServerClient mockServerClient;

    @BeforeAll
    public static void startServer() {
        mockServerClient = ClientAndServer.startClientAndServer(8081);
        mockServerClient.when(
                request()
                    .withMethod("POST")
                    .withPath(NottableString.string("/api/course-request"))
                    .withHeader(NottableString.string(".*")),
                Times.unlimited())
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Cache-Control", "public, max-age=86400"))
                    .withDelay(TimeUnit.MICROSECONDS,200)
            );
    }

    @AfterAll
    public static void stopServer() {
        mockServerClient.stop();
    }


    @Test
    void should_call_mockserver_success_when_call_client() throws Exception {

        CourseRequestDto requestDto = CourseRequestDto.builder()
            .name("LinearAlgebra")
            .type(CourseType.HIGHER_MATHEMATICS)
            .contractId(1L)
            .createdAt(Instant.parse("2023-06-01T00:00:00Z"))
            .expiredAt(Instant.parse("2099-07-01T00:00:00Z"))
            .build();

        courseContentClient.submitCourseRequest(requestDto);

        mockServerClient.verify(request().withMethod("POST")
                .withPath(NottableString.string("/api/course-request"))
                .withHeader(NottableString.string(".*")),
            VerificationTimes.exactly(1)
        );

    }

}