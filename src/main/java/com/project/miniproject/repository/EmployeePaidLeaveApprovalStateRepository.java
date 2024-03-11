package com.project.miniproject.repository;

import com.project.miniproject.model.EmployeePaidLeaveApprovalState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePaidLeaveApprovalStateRepository extends JpaRepository<EmployeePaidLeaveApprovalState, Integer> {
    @Query(nativeQuery = true,
    value = "SELECT * from employee_paid_leave_approval_state WHERE id =:requestNumber")
    EmployeePaidLeaveApprovalState findRequestNumberById(@Param("requestNumber") Integer requestNumber);
}
