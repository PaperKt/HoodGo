package com.hoodgo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoodgo.constant.MessageConstant;
import com.hoodgo.constant.PasswordConstant;
import com.hoodgo.constant.StatusConstant;
import com.hoodgo.context.BaseContext;
import com.hoodgo.dto.EmployeeDTO;
import com.hoodgo.dto.EmployeeLoginDTO;
import com.hoodgo.dto.EmployeePageQueryDTO;
import com.hoodgo.dto.PasswordEditDTO;
import com.hoodgo.entity.Employee;
import com.hoodgo.exception.AccountLockedException;
import com.hoodgo.exception.AccountNotFoundException;
import com.hoodgo.exception.PasswordErrorException;
import com.hoodgo.mapper.EmployeeMapper;
import com.hoodgo.result.PageResult;
import com.hoodgo.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        String passwordMD5 = DigestUtils.md5DigestAsHex(password.getBytes());

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        if (!passwordMD5.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    public void addEmployee(EmployeeDTO employeeDTO) {
        // 对密码进行MD5加密
        Employee employee = Employee.builder()
                .username(employeeDTO.getUsername())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .idNumber(employeeDTO.getIdNumber())
                .sex(employeeDTO.getSex())
                .build();
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());


        // 保存员工信息到数据库
        employeeMapper.insert(employee);
    }


    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());

    }

    public void startOrStop(int status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);

    }


    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    public void updateEmployee(EmployeeDTO employeeDTO) {
/*        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .idNumber(employeeDTO.getIdNumber())
                .sex(employeeDTO.getSex())
                .username(employeeDTO.getUsername())
                .build();*/
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

    public void editPassword(PasswordEditDTO passwordEditDTO) {
        String pwd = employeeMapper.getById(passwordEditDTO.getEmpId()).getPassword();
        if(!pwd.equals(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()))){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        Employee employee = new Employee();
        employee.setId(passwordEditDTO.getEmpId());
        employee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        employeeMapper.update(employee);
    }
}
