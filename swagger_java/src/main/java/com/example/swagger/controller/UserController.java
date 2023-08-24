package com.example.swagger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.swagger.common.R;
import com.example.swagger.entity.User;
import com.example.swagger.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 员工登录
     *
     * @param request
     * @param user
     * @return
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User emp = userService.getOne(queryWrapper);
        if (emp == null) {
            return R.error("登录失败");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("user", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     *
     * @return
     */
    @ApiOperation("退出")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param user
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody User user) {
        log.info("新增员工，员工信息：{}", user.toString());
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        user.setCreateUser(1L);
//        user.setUpdateUser(1L);

        userService.save(user);
        return R.success("新增员工成功");
    }


}
