package learning.way.lecturer.management.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResultDTO {
    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("message")
    private String message;
}
