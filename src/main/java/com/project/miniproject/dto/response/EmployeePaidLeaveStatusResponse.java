package com.project.miniproject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePaidLeaveStatusResponse {
    private Integer paidLeaveRequestTicketNumber;

    private String paidLeaveRequestTicketStatus;

    private String employeeId;

    private String paidLeaveReason;

    private Integer requestedPaidLeaveNumber;

    private Integer paidLeaveLimit;

    private String responseCode;

    private String responseBody;

}
