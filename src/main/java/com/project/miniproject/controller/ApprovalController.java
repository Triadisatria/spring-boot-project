package com.project.miniproject.controller;

import com.project.miniproject.dto.request.EmployeeRegistrationRequest;
import com.project.miniproject.dto.request.EmployeeUpdateRequest;
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
}
