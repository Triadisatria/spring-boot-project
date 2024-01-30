package com.project.miniproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Employee")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employeeId")
    private String employeeId;

    @Column(name = "employeeName")
    private String employeeName;

    @Column(name = "roles")
    private String roles;

    @Column(name = "dept")
    private String dept;

    @Column(name = "paidLeaveLimit")
    private Integer paidLeaveLimit;
}
