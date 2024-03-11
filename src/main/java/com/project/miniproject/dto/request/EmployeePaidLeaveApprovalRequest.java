package com.project.miniproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeePaidLeaveApprovalRequest {
    private String paidLeaveRequester;

    private String paidLeaveApprover;
}
