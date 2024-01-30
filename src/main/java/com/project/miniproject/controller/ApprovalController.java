package com.project.miniproject.controller;

import com.project.miniproject.dto.request.EmployeeRegistrationRequest;
import com.project.miniproject.dto.response.EmployeeRegistrationResponse;
import com.project.miniproject.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("approval-service")
public class ApprovalController {
    @Autowired
    ApprovalService approvalService;

    @PostMapping("/employee/register")
    public ResponseEntity<EmployeeRegistrationResponse> registerEmployee(@RequestBody EmployeeRegistrationRequest registrationInput){
        return approvalService.employeeRegistration(registrationInput);
    }
}
