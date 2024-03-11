package com.project.miniproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeePaidLeaveRequest {
    private String reason;

    private Integer requestedPaidLeaveNumber;
}
