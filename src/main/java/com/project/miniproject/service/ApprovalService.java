package com.project.miniproject.service;

import com.project.miniproject.dto.request.EmployeeRegistrationRequest;
import com.project.miniproject.dto.response.EmployeeRegistrationResponse;
import com.project.miniproject.exception.RequestException;
import com.project.miniproject.model.Employee;
import com.project.miniproject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ApprovalService {
    @Autowired
    EmployeeRepository employeeRepository;
    public ResponseEntity<EmployeeRegistrationResponse> employeeRegistration(EmployeeRegistrationRequest registrationRequest){
        EmployeeRegistrationResponse registrationResponse = new EmployeeRegistrationResponse();
        SecureRandom random = new SecureRandom();

        int ranum = random.nextInt(10, 99);
        String nameInput = registrationRequest.getEmployeeName();
        String deptInput = registrationRequest.getDept();
        String roleInput = registrationRequest.getRoles();
        String eid = "EID.";
        String uniqueId = eid
                .concat(String.valueOf(deptInput.charAt(0)))
                .concat(String.valueOf(nameInput.length()))
                .concat(String.valueOf(nameInput.charAt(0)))
                .concat(String.valueOf(ranum));

        registrationResponse.setEmployeeId(uniqueId);
        registrationResponse.setRoles(registrationRequest.getRoles());

        Employee employee = new Employee();
        employee.setEmployeeId(uniqueId);
        employee.setEmployeeName(registrationRequest.getEmployeeName());
        employee.setDept(registrationRequest.getDept());

        switch (roleInput){
            case "Staff", "ManagerOfManager", "Manager", "ChiefLevel":
                employee.setRoles(registrationRequest.getRoles());
                break;
            default:
                throw new RequestException("Error: Invalid role input");
        }
        switch (roleInput){
            case "Staff":
                employee.setPaidLeaveLimit(13);
                break;
            case "Manager":
                employee.setPaidLeaveLimit(14);
                break;
            case "ManagerOfManager":
                employee.setPaidLeaveLimit(15);
                break;
            case "ChiefLevel":
                employee.setPaidLeaveLimit(16);
                break;
            default:
                throw new RequestException("Error: Invalid role input");
        }

        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }
}
