package com.project.miniproject.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String responseCode;
    private String responseMessage;
}
