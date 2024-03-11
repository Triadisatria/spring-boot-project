package com.project.miniproject.service;

import com.project.miniproject.dto.request.EmployeePaidLeaveApprovalRequest;
import com.project.miniproject.dto.request.EmployeePaidLeaveRequest;
import com.project.miniproject.dto.request.EmployeeRegistrationRequest;
import com.project.miniproject.dto.request.EmployeeUpdateRequest;
import com.project.miniproject.dto.response.EmployeePaidLeaveResponse;
import com.project.miniproject.dto.response.EmployeePaidLeaveStatusResponse;
import com.project.miniproject.dto.response.EmployeeRegistrationResponse;
import com.project.miniproject.dto.response.EmployeeUDResponse;
import com.project.miniproject.exception.RequestException;
import com.project.miniproject.model.Employee;
import com.project.miniproject.model.EmployeePaidLeaveApprovalState;
import com.project.miniproject.repository.EmployeePaidLeaveApprovalStateRepository;
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
    @Autowired
    EmployeePaidLeaveApprovalStateRepository paidLeaveApprovalStateRepository;
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
    public ResponseEntity<EmployeePaidLeaveResponse> paidLeaveSubmit(String EID, EmployeePaidLeaveRequest paidLeaveRequest){
        EmployeePaidLeaveResponse employeePaidLeaveResponse = new EmployeePaidLeaveResponse();
        EmployeePaidLeaveApprovalState paidLeaveApprovalState = new EmployeePaidLeaveApprovalState();

        Employee paidLeaveRequester = employeeRepository.findEmployeeByEID(EID);
        Integer paidLeaveLimit = paidLeaveRequester.getPaidLeaveLimit();

        if (paidLeaveRequest.getReason() == null || paidLeaveRequest.getRequestedPaidLeaveNumber() == null){
            throw new RequestException("Error: Please fill the paid leave reason and the requested paid leave number", HttpStatus.BAD_REQUEST);
        } else if (paidLeaveRequest.getRequestedPaidLeaveNumber() == 0){
            throw new RequestException("Error: Requested paid leave number cannot be 0", HttpStatus.BAD_REQUEST);
        }else if (paidLeaveRequest.getRequestedPaidLeaveNumber() > paidLeaveLimit){
            throw new RequestException("Error: Requested paid leave number cannot exceed paid leave limit", HttpStatus.BAD_REQUEST);
        }else {
            employeePaidLeaveResponse.setResponseCode(String.valueOf(HttpStatus.OK));
            employeePaidLeaveResponse.setResponseBody("Paid leave request has been submitted");

            paidLeaveApprovalState.setEmployeeId(EID);
            paidLeaveApprovalState.setReason(paidLeaveRequest.getReason());
            paidLeaveApprovalState.setPaidLeaveNumber(paidLeaveRequest.getRequestedPaidLeaveNumber());
            paidLeaveApprovalState.setApproval1(false);
            paidLeaveApprovalState.setApproval2(false);
            paidLeaveApprovalState.setApprovalStatus("Waiting approval");

            paidLeaveApprovalStateRepository.save(paidLeaveApprovalState);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeePaidLeaveResponse);
    }
    public ResponseEntity<EmployeePaidLeaveResponse> firstPaidLeaveApproval(Integer requestNumber, EmployeePaidLeaveApprovalRequest paidLeaveApprovalRequest){
        EmployeePaidLeaveResponse paidLeaveResponse = new EmployeePaidLeaveResponse();
        EmployeePaidLeaveApprovalState paidLeaveRequest = paidLeaveApprovalStateRepository.findRequestNumberById(requestNumber);

        Employee requester = employeeRepository.findEmployeeByEID(paidLeaveApprovalRequest.getPaidLeaveRequester());
        String requesterRole = requester.getRoles();

        Employee approver = employeeRepository.findEmployeeByEID(paidLeaveApprovalRequest.getPaidLeaveApprover());
        String approverRole = approver.getRoles();

        String approverEID = paidLeaveRequest.getApprovalSign1();

        if (paidLeaveRequest.getApprovalStatus().equals("Waiting 2nd approval")){
            throw new RequestException("Error: First approval has been done by: " + approverEID, HttpStatus.BAD_REQUEST);
        }else if (requesterRole.equals("Staff")){
            switch (approverRole){
                case "Manager", "ManagerOfManager", "ChiefLevel":
                    paidLeaveRequest.setApproval1(true);
                    paidLeaveRequest.setApprovalSign1(paidLeaveApprovalRequest.getPaidLeaveApprover());
                    paidLeaveRequest.setApprovalStatus("Waiting 2nd approval");
                    break;
                default:
                    throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("Manager")){
            switch (approverRole){
                case "ManagerOfManager", "ChiefLevel":
                    paidLeaveRequest.setApproval1(true);
                    paidLeaveRequest.setApprovalSign1(paidLeaveApprovalRequest.getPaidLeaveApprover());
                    paidLeaveRequest.setApprovalStatus("Waiting 2nd approval");
                    break;
                default:
                    throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("ManagerOfManager")){
            if (approverRole.equals("ChiefLevel")) {
                paidLeaveRequest.setApproval1(true);
                paidLeaveRequest.setApprovalSign1(paidLeaveApprovalRequest.getPaidLeaveApprover());
                paidLeaveRequest.setApprovalStatus("Waiting 2nd approval");
            } else {
                throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("ChiefLevel")){
            if (approverRole.equals("ChiefLevel")) {
                paidLeaveRequest.setApproval1(true);
                paidLeaveRequest.setApprovalSign1(paidLeaveApprovalRequest.getPaidLeaveApprover());
                paidLeaveRequest.setApprovalStatus("Waiting 2nd approval");
            } else {
                throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }
        paidLeaveResponse.setResponseCode(String.valueOf(HttpStatus.OK));
        paidLeaveResponse.setResponseBody("Paid leave request first approval has been completed");

        paidLeaveApprovalStateRepository.save(paidLeaveRequest);

        return ResponseEntity.status(HttpStatus.OK).body(paidLeaveResponse);
    }
    public ResponseEntity<EmployeePaidLeaveResponse> secondPaidLeaveApproval(Integer requestNumber, EmployeePaidLeaveApprovalRequest paidLeaveApprovalRequest){
        EmployeePaidLeaveResponse paidLeaveResponse = new EmployeePaidLeaveResponse();
        EmployeePaidLeaveApprovalState paidLeaveRequest = paidLeaveApprovalStateRepository.findRequestNumberById(requestNumber);

        Employee requester = employeeRepository.findEmployeeByEID(paidLeaveApprovalRequest.getPaidLeaveRequester());
        String requesterRole = requester.getRoles();
        Integer requesterPaidLeaveLimit = requester.getPaidLeaveLimit();

        Employee approver2 = employeeRepository.findEmployeeByEID(paidLeaveApprovalRequest.getPaidLeaveApprover());
        String secondApproverRole = approver2.getRoles();

        String firstApproverEID = paidLeaveRequest.getApprovalSign1();

        if (paidLeaveRequest.getApprovalStatus().equals("Paid leave has been approved")){
            throw new RequestException("Error: Paid leave request number " + requestNumber + " has been closed", HttpStatus.BAD_REQUEST);
        }else if (firstApproverEID == null){
            throw new RequestException("Error: Second approval cannot be authorize if the first approval hasn't been done", HttpStatus.BAD_REQUEST);
        }else if (firstApproverEID.equals(paidLeaveApprovalRequest.getPaidLeaveApprover())){
            throw new RequestException("Error: One person cannot give multiple approval on the same paid leave request", HttpStatus.BAD_REQUEST);
        }else if (requesterRole.equals("Staff")){
            switch (secondApproverRole){
                case "Manager", "ManagerOfManager", "ChiefLevel":
                    paidLeaveRequest.setApproval2(true);
                    paidLeaveRequest.setApprovalSign2(paidLeaveApprovalRequest.getPaidLeaveApprover());
                    paidLeaveRequest.setApprovalStatus("Paid leave has been approved");

                    requester.setPaidLeaveLimit(requesterPaidLeaveLimit - paidLeaveRequest.getPaidLeaveNumber());
                    break;
                default:
                    throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("Manager")){
            switch (secondApproverRole){
                case "ManagerOfManager", "ChiefLevel":
                    paidLeaveRequest.setApproval2(true);
                    paidLeaveRequest.setApprovalSign2(paidLeaveApprovalRequest.getPaidLeaveApprover());
                    paidLeaveRequest.setApprovalStatus("Paid leave has been approved");

                    requester.setPaidLeaveLimit(requesterPaidLeaveLimit - paidLeaveRequest.getPaidLeaveNumber());
                    break;
                default:
                    throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("ManagerOfManager")){
            if (secondApproverRole.equals("ChiefLevel")){
                paidLeaveRequest.setApproval2(true);
                paidLeaveRequest.setApprovalSign2(paidLeaveApprovalRequest.getPaidLeaveApprover());
                paidLeaveRequest.setApprovalStatus("Paid leave has been approved");

                requester.setPaidLeaveLimit(requesterPaidLeaveLimit - paidLeaveRequest.getPaidLeaveNumber());
            }else {
                throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }else if (requesterRole.equals("ChiefLevel")){
            if (secondApproverRole.equals("ChiefLevel")){
                paidLeaveRequest.setApproval2(true);
                paidLeaveRequest.setApprovalSign2(paidLeaveApprovalRequest.getPaidLeaveApprover());
                paidLeaveRequest.setApprovalStatus("Paid leave has been approved");

                requester.setPaidLeaveLimit(requesterPaidLeaveLimit - paidLeaveRequest.getPaidLeaveNumber());
            }else {
                throw new RequestException("Error: invalid or insufficient role authority for approving paid leave", HttpStatus.BAD_REQUEST);
            }
        }

        paidLeaveResponse.setResponseCode(String.valueOf(HttpStatus.OK));
        paidLeaveResponse.setResponseBody("Paid leave request second approval has been completed");

        employeeRepository.save(requester);
        paidLeaveApprovalStateRepository.save(paidLeaveRequest);

        return ResponseEntity.status(HttpStatus.OK).body(paidLeaveResponse);
    }
    public ResponseEntity<EmployeePaidLeaveStatusResponse> paidLeaveStatus(Integer requestNumber){
        EmployeePaidLeaveStatusResponse paidLeaveStatusResponse = new EmployeePaidLeaveStatusResponse();
        EmployeePaidLeaveApprovalState paidLeaveRequest = paidLeaveApprovalStateRepository.findRequestNumberById(requestNumber);
        Employee requester = employeeRepository.findEmployeeByEID(paidLeaveRequest.getEmployeeId());

        paidLeaveStatusResponse.setResponseCode(String.valueOf(HttpStatus.OK));
        paidLeaveStatusResponse.setPaidLeaveRequestTicketNumber(requestNumber);
        paidLeaveStatusResponse.setPaidLeaveRequestTicketStatus(paidLeaveRequest.getApprovalStatus());
        paidLeaveStatusResponse.setEmployeeId(paidLeaveRequest.getEmployeeId());
        paidLeaveStatusResponse.setPaidLeaveReason(paidLeaveRequest.getReason());
        paidLeaveStatusResponse.setRequestedPaidLeaveNumber(paidLeaveRequest.getPaidLeaveNumber());
        paidLeaveStatusResponse.setPaidLeaveLimit(requester.getPaidLeaveLimit());
        paidLeaveStatusResponse.setResponseBody("Paid leave limit has already been reduced by requested paid leave number if the paid leave request ticket status: Paid leave has been approved");

        return ResponseEntity.status(HttpStatus.OK).body(paidLeaveStatusResponse);
    }
}
