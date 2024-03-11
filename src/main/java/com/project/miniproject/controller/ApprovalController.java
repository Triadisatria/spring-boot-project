package com.project.miniproject.controller;

import com.project.miniproject.dto.request.*;
import com.project.miniproject.dto.response.EmployeePaidLeaveResponse;
import com.project.miniproject.dto.response.EmployeePaidLeaveStatusResponse;
import com.project.miniproject.dto.response.EmployeeRegistrationResponse;
import com.project.miniproject.dto.response.EmployeeUDResponse;
import com.project.miniproject.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("approval-service")
public class ApprovalController {
    @Autowired
    ApprovalService approvalService;

    @PostMapping("/employee/register")
    public ResponseEntity<EmployeeRegistrationResponse> registerEmployee(@RequestBody EmployeeRegistrationRequest registrationInput){
        return approvalService.employeeRegistration(registrationInput);
    }
    @PutMapping("/update/{employeeId}")
    public ResponseEntity<EmployeeUDResponse> updateEmployee(@PathVariable(value = "employeeId") String EID, @RequestBody EmployeeUpdateRequest employeeUpdateInput){
        return approvalService.employeeUpdates(EID, employeeUpdateInput);
    }
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<EmployeeUDResponse> deleteEmployee(@PathVariable(value = "employeeId") String EID){
        return approvalService.employeeDeletion(EID);
    }
    @PostMapping("/submit/leave/{employeeId}")
    public ResponseEntity<EmployeePaidLeaveResponse> submitPaidLeave(@PathVariable(value = "employeeId") String EID, @RequestBody EmployeePaidLeaveRequest paidLeaveRequest){
        return approvalService.paidLeaveSubmit(EID, paidLeaveRequest);
    }
    @GetMapping("/request/leave/{requestNumber}")
    public ResponseEntity<EmployeePaidLeaveStatusResponse> getPaidLeaveStatus(@PathVariable(value = "requestNumber") Integer requestNumber){
        return approvalService.paidLeaveStatus(requestNumber);
    }
    @PutMapping("/approval/leave/first/{requestNumber}")
    public ResponseEntity<EmployeePaidLeaveResponse> approvalPaidLeaveFirst(@PathVariable(value = "requestNumber") Integer requestNumber, @RequestBody EmployeePaidLeaveApprovalRequest paidLeaveApprovalRequest){
        return approvalService.firstPaidLeaveApproval(requestNumber, paidLeaveApprovalRequest);
    }
    @PutMapping("approval/leave/second/{requestNumber}")
    public ResponseEntity<EmployeePaidLeaveResponse> approvalPaidLeaveSecond(@PathVariable(value = "requestNumber") Integer requestNumber, @RequestBody EmployeePaidLeaveApprovalRequest paidLeaveApprovalRequest){
        return approvalService.secondPaidLeaveApproval(requestNumber, paidLeaveApprovalRequest);
    }
}
