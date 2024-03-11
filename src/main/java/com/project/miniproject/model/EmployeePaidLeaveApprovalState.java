package com.project.miniproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EmployeePaidLeaveApprovalState")
@Getter
@Setter
public class EmployeePaidLeaveApprovalState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "EmployeeId")
    private String employeeId;

    @Column(name = "Reason")
    private String reason;

    @Column(name = "RequestedPaidLeaveNumber")
    private Integer paidLeaveNumber;

    @Column(name = "Approval 1")
    private boolean approval1;

    @Column(name = "ApprovalSignature1")
    private String approvalSign1;

    @Column(name = "Approval 2")
    private boolean approval2;

    @Column(name = "ApprovalSignature2")
    private String approvalSign2;

    @Column(name = "ApprovalStatus")
    private String approvalStatus;
}
