package com.project.miniproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRegistrationRequest {
    @NotBlank
    private String employeeName;
    @NotBlank
    private String roles;
    @NotBlank
    private String dept;
}
