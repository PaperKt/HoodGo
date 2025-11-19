package com.hoodgo.service;

import com.hoodgo.dto.EmployeeDTO;
import com.hoodgo.dto.EmployeeLoginDTO;
import com.hoodgo.dto.EmployeePageQueryDTO;
import com.hoodgo.dto.PasswordEditDTO;
import com.hoodgo.entity.Employee;
import com.hoodgo.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    void addEmployee(EmployeeDTO employeeDTO);
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(int status, Long id);

    Employee getById(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);

    void editPassword(PasswordEditDTO passwordEditDTO);
}
