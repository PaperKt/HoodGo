package com.hoodgo.controller.admin;

import com.hoodgo.constant.JwtClaimsConstant;
import com.hoodgo.context.BaseContext;
import com.hoodgo.dto.EmployeeDTO;
import com.hoodgo.dto.EmployeeLoginDTO;
import com.hoodgo.dto.EmployeePageQueryDTO;
import com.hoodgo.dto.PasswordEditDTO;
import com.hoodgo.entity.Employee;
import com.hoodgo.properties.JwtProperties;
import com.hoodgo.result.PageResult;
import com.hoodgo.result.Result;
import com.hoodgo.service.EmployeeService;
import com.hoodgo.utils.JwtUtil;
import com.hoodgo.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录接口")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录接口")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping()
    @ApiOperation(value = "添加员工接口")
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工：{}", employeeDTO.getUsername());
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询接口")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用员工接口")
    public Result startOrStop(@PathVariable int status,Long id ) {
        log.info("启用禁用员工：{}",id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工接口")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工：{}",id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping()
    @ApiOperation("修改员工接口")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工：{}",employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    @PutMapping("/editPassword")
    @ApiOperation("修改员工密码接口")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        if(passwordEditDTO.getEmpId() == null)
            passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        log.info("修改员工密码：{}",passwordEditDTO.getEmpId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
