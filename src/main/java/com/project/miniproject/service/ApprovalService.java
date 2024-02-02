package com.project.miniproject.service;

import com.project.miniproject.dto.request.EmployeeRegistrationRequest;
import com.project.miniproject.dto.request.EmployeeUpdateRequest;
import com.project.miniproject.dto.response.EmployeeRegistrationResponse;
import com.project.miniproject.dto.response.EmployeeUDResponse;
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

        int ranum = random.nextInt(100, 999);
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
                throw new RequestException("Error: Invalid role input", HttpStatus.BAD_REQUEST);
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
                throw new RequestException("Error: Invalid role input", HttpStatus.BAD_REQUEST);
        }

        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }
    public ResponseEntity<EmployeeUDResponse> employeeDeletion(String EID){
        EmployeeUDResponse employeeUDResponse = new EmployeeUDResponse();

        employeeUDResponse.setResponseCode(String.valueOf(HttpStatus.OK));
        employeeUDResponse.setResponseMessage("Data has been deleted");

        if (employeeRepository.existsByEmployeeId(EID)){
            employeeRepository.deleteEmployeeByEID(EID);
        }else {
            throw new RequestException("Employee not exist with EID: " + EID, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(employeeUDResponse);
    }
    public ResponseEntity<EmployeeUDResponse> employeeUpdates(String EID, EmployeeUpdateRequest employeeUpdateInput){
        EmployeeUDResponse employeeUDResponse = new EmployeeUDResponse();
        Employee employee = employeeRepository.findEmployeeByEID(EID);
        String roleInput = employeeUpdateInput.getRoles();
        String deptInput = employeeUpdateInput.getDept();

        if (roleInput == null){
            throw new RequestException("Error: Roles cannot be null", HttpStatus.BAD_REQUEST);
        }else {
            switch (roleInput){
                case "Staff", "ManagerOfManager", "Manager", "ChiefLevel":
                    employee.setRoles(employeeUpdateInput.getRoles());
                    break;
                default:
                    throw new RequestException("Error: Invalid role input", HttpStatus.NOT_MODIFIED);
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
                    throw new RequestException("Error: Invalid role input", HttpStatus.NOT_MODIFIED);
            }
        }

        if (deptInput != null){
            switch (deptInput){
                case "Marketing", "Human Resources"
                        , "Accounting", "Production"
                        , "Information Technology":
                    employee.setDept(employeeUpdateInput.getDept());
                    break;
                default:
                    throw new RequestException("Error: Invalid dept input", HttpStatus.NOT_MODIFIED);
            }
        }

        employeeUDResponse.setResponseCode(String.valueOf(HttpStatus.OK));
        employeeUDResponse.setResponseMessage("Data has been updated");

        employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.OK).body(employeeUDResponse);
    }
}
