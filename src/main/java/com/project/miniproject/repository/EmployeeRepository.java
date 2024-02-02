package com.project.miniproject.repository;

import com.project.miniproject.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM employee WHERE employee_id =:EID")
    Employee findEmployeeByEID(@Param("EID") String EID);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "DELETE FROM employee WHERE employee_id =:EID")
    void deleteEmployeeByEID(@Param("EID") String EID);

    boolean existsByEmployeeId(String EID);
}
