package com.zapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.User;
import com.zapi.mapper.UserMapper;
import com.zapi.service.UserService;
import com.zapi.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private com.zapi.service.SystemStatisticsService systemStatisticsService;
    
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String captcha) {
        // 验证验证码
        if (captcha == null || captcha.isEmpty()) {
            return Result.error("请扫描下方二维码获取正确的验证码输入");
        }
        
        if (!"1121".equals(captcha)) {
            return Result.error("请扫描下方二维码获取正确的验证码输入");
        }
        try {
            
            // 创建认证token
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(username, password);
            
            // 进行认证
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            if (authentication.isAuthenticated()) {
                // 获取用户详情
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                
                // 从数据库获取完整的用户信息
                User user = userService.findByUsername(userDetails.getUsername());
                if (user == null) {
                    return Result.error("用户信息不存在");
                }
                
                // 生成JWT token，使用实际的用户ID
                String token = jwtUtil.generateToken(user.getUsername(), user.getId());
                
                // 更新最后登录时间
                user.setLastLoginTime(java.time.LocalDateTime.now());
                userService.updateById(user);
                
                // 记录登录统计
                try {
                    systemStatisticsService.recordLogin();
                } catch (Exception e) {
                    // 统计失败不影响登录
                }
                
                // 构建返回数据
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("realName", user.getRealName());
                userInfo.put("email", user.getEmail());
                userInfo.put("status", user.getStatus());
                userInfo.put("lastLoginTime", user.getLastLoginTime());
                userInfo.put("createTime", user.getCreateTime());
                userInfo.put("updateTime", user.getUpdateTime());
                
                data.put("user", userInfo);
                
                return Result.success(data);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 临时方法：更新admin用户密码
     */
    @ApiOperation("更新admin用户密码")
    @PostMapping("/updatePassword")
    public Result<String> updatePassword() {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPassword = encoder.encode("123456");
            
            // 更新密码
            int result = userMapper.updatePasswordByUsername("admin", newPassword);
            
            if (result > 0) {
                return Result.success("密码更新成功，新密码: " + newPassword);
            } else {
                return Result.error("密码更新失败：未找到admin用户");
            }
        } catch (Exception e) {
            return Result.error("密码更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public Result<Page<User>> getUserList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long orgId) {
        try {
            Page<User> page = new Page<>(current, size);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            
            // 如果指定了组织ID，则只查询该组织下的用户
            if (orgId != null) {
                // 先获取该组织下的所有用户ID
                List<Long> userIds = userMapper.selectUserIdsByOrgId(orgId);
                if (userIds == null || userIds.isEmpty()) {
                    // 如果该组织下没有用户，返回空结果
                    return Result.success(page);
                }
                queryWrapper.in("id", userIds);
            }
            
            // 添加关键词搜索条件（搜索用户名、真实姓名、邮箱）
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchKeyword = keyword.trim();
                queryWrapper.and(wrapper -> 
                    wrapper.like("username", searchKeyword)
                           .or().like("real_name", searchKeyword)
                           .or().like("email", searchKeyword)
                );
            }
            
            // 兼容旧的realName参数
            if (realName != null && !realName.trim().isEmpty()) {
                queryWrapper.like("real_name", realName.trim());
            }
            
            // 状态筛选
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            // 只查询未删除的用户
            queryWrapper.eq("deleted", 0);
            
            // 按创建时间倒序排列
            queryWrapper.orderByDesc("create_time");
            
            Page<User> result = userService.page(page, queryWrapper);
            
            // 清除返回结果中的密码字段
            result.getRecords().forEach(user -> user.setPassword(null));
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户详情
     */
    @ApiOperation("获取用户详情")
    @GetMapping("/detail/{id}")
    public Result<User> getUserDetail(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user == null || user.getDeleted() == 1) {
                return Result.error("用户不存在");
            }
            
            // 清除敏感信息
            user.setPassword(null);
            
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 添加用户
     */
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", user.getUsername());
            queryWrapper.eq("deleted", 0);
            User existingUser = userService.getOne(queryWrapper);
            if (existingUser != null) {
                return Result.error("用户名已存在");
            }
            
            // 检查邮箱是否已存在
            if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                QueryWrapper<User> emailQuery = new QueryWrapper<>();
                emailQuery.eq("email", user.getEmail());
                emailQuery.eq("deleted", 0);
                User existingEmailUser = userService.getOne(emailQuery);
                if (existingEmailUser != null) {
                    return Result.error("邮箱已存在");
                }
            }
            
            // 加密密码
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
            } else {
                return Result.error("密码不能为空");
            }
            
            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认启用
            }
            user.setDeleted(0); // 未删除
            user.setCreateTime(java.time.LocalDateTime.now());
            user.setUpdateTime(java.time.LocalDateTime.now());
            
            boolean success = userService.save(user);
            if (success) {
                return Result.success("添加用户成功");
            } else {
                return Result.error("添加用户失败");
            }
        } catch (Exception e) {
            return Result.error("添加用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户
     */
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        try {
            // 检查用户是否存在
            User existingUser = userService.getById(user.getId());
            if (existingUser == null || existingUser.getDeleted() == 1) {
                return Result.error("用户不存在");
            }
            
            // 如果修改了用户名，检查是否与其他用户重复
            if (!existingUser.getUsername().equals(user.getUsername())) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", user.getUsername());
                queryWrapper.eq("deleted", 0);
                queryWrapper.ne("id", user.getId());
                User duplicateUser = userService.getOne(queryWrapper);
                if (duplicateUser != null) {
                    return Result.error("用户名已存在");
                }
            }
            
            // 如果修改了邮箱，检查是否与其他用户重复
            if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
                QueryWrapper<User> emailQuery = new QueryWrapper<>();
                emailQuery.eq("email", user.getEmail());
                emailQuery.eq("deleted", 0);
                emailQuery.ne("id", user.getId());
                User duplicateEmailUser = userService.getOne(emailQuery);
                if (duplicateEmailUser != null) {
                    return Result.error("邮箱已存在");
                }
            }
            
            // 保留原有的密码和创建时间
            user.setPassword(existingUser.getPassword());
            user.setCreateTime(existingUser.getCreateTime());
            user.setUpdateTime(java.time.LocalDateTime.now());
            
            boolean success = userService.updateById(user);
            if (success) {
                return Result.success("更新用户成功");
            } else {
                return Result.error("更新用户失败");
            }
        } catch (Exception e) {
            return Result.error("更新用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户状态
     */
    @ApiOperation("更新用户状态")
    @PutMapping("/updateStatus/{id}")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            // 检查用户是否存在
            User existingUser = userService.getById(id);
            if (existingUser == null || existingUser.getDeleted() == 1) {
                return Result.error("用户不存在");
            }
            
            // 更新状态
            existingUser.setStatus(status);
            boolean success = userService.updateById(existingUser);
            if (success) {
                String statusText = status == 1 ? "启用" : "禁用";
                return Result.success(statusText + "用户成功");
            } else {
                return Result.error("更新用户状态失败");
            }
        } catch (Exception e) {
            return Result.error("更新用户状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        try {
            // 检查用户是否存在
            User existingUser = userService.getById(id);
            if (existingUser == null || existingUser.getDeleted() == 1) {
                return Result.error("用户不存在");
            }
            
            // 逻辑删除
            existingUser.setDeleted(1);
            boolean success = userService.updateById(existingUser);
            if (success) {
                return Result.success("删除用户成功");
            } else {
                return Result.error("删除用户失败");
            }
        } catch (Exception e) {
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有启用用户
     */
    @ApiOperation("获取所有启用用户")
    @GetMapping("/enabled")
    public Result<java.util.List<User>> getEnabledUsers() {
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            
            // 只查询启用状态的用户
            queryWrapper.eq("status", 1);
            
            // 只查询未删除的用户
            queryWrapper.eq("deleted", 0);
            
            // 按创建时间倒序排列
            queryWrapper.orderByDesc("create_time");
            
            java.util.List<User> users = userService.list(queryWrapper);
            
            // 清除返回结果中的密码字段
            users.forEach(user -> user.setPassword(null));
            
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("获取启用用户列表失败：" + e.getMessage());
        }
    }

}