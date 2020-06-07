package com.interview.template.configs;


import lombok.Data;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ErrorDetails {

    private String error;
    private Integer status;

    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorDetails(HttpStatus httpStatus, String errorDescription) {

        this.error = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.errorDescription = errorDescription;

    }

    public ErrorDetails(String error, Integer status, String errorDescription) {

        this.error = error;
        this.status = status;
        this.errorDescription = errorDescription;

    }

}
