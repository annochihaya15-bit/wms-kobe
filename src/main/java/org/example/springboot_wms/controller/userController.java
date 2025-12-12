package org.example.springboot_wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import org.example.springboot_wms.common.AdminRequired;
import org.example.springboot_wms.common.LoginDTO;
import org.example.springboot_wms.common.Result;
import org.example.springboot_wms.common.UserQueryDTO;
import org.example.springboot_wms.domain.user;
import org.example.springboot_wms.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/user")
public class userController {
    private static final String USER_KEY = "loginUser";
    @Value("${upload.avatar.path}")
    private String avatarUploadPath; // 本地存储路径
    @Value("${upload.avatar.url}")
    private String avatarUrlPrefix;  // 前端访问前缀

    @GetMapping("/hello")
    public String sayhello() {
        return "Hello Wms!";
    }

    @Autowired
    private userservice userservice;

    @GetMapping("/test")
    public String test() {
        return "UserController工作正常！时间：" + System.currentTimeMillis();
    }

    @GetMapping("/list")
    public Result<List<user>> list() {
        List<user> userList = userservice.list();
        return Result.success(userList);
    }

    //service服务作用是把封装好的查询数据返回前端
    @GetMapping("/科比坠机")
    public Object listAll() {
        List<user> userList = userservice.listAll();
        return Result.success(userList);
    }

    @Transactional//事务会自动回滚
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody user user) {
            boolean isSuccess = userservice.save(user);
            if (isSuccess) {
                return Result.success("保存用户成功");
            } else {
                return Result.error("保存用户失败");
            }

    }

    @Transactional
    @AdminRequired
    @PostMapping("/mod")
    public Result<Object> mod(@RequestBody user user, HttpSession session) {
      userservice.moduser( user, session);
            return Result.success("修改成功");
    }

    @Transactional
    @AdminRequired
    @GetMapping("/delete")
    public Object delete(int id,HttpSession  session) {

            return Result.success("删除用户成功");
    }

    // 修正后的搜索方法
    @PostMapping("/search")
    public Result<List<user>> searchUsers(@RequestBody user user) {
        LambdaQueryWrapper<user> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(org.example.springboot_wms.domain.user::getName, user.getName());
        List<user> userList = userservice.list(wrapper);
        return Result.success(userList);
    }

    @PostMapping("/listPage")
    public Result<Page<user>> listPage(@RequestBody UserQueryDTO queryDTO) {
        // 1. 分页逻辑（原代码不变）
        Page<user> page = new Page<>(queryDTO.getCurrentPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<user> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getName() != null && !queryDTO.getName().isEmpty()) {
            wrapper.like(user::getName, queryDTO.getName());
        }
        if (queryDTO.getMinAge() != null) {
            wrapper.ge(user::getAge, queryDTO.getMinAge());
        }
        if (queryDTO.getMaxAge() != null) {
            wrapper.le(user::getAge, queryDTO.getMaxAge());
        }
        if (queryDTO.getSex() != null) {
            wrapper.eq(user::getSex, queryDTO.getSex());
        }
        if (queryDTO.getPhone() != null) {
            wrapper.like(user::getPhone, queryDTO.getPhone());
        }
        Page<user> resultPage = userservice.page(page, wrapper);// 3. 执行分页查询

        // 2. 用Result封装后返回
        return Result.success(resultPage);
    }

    @GetMapping("/getById")
    public Result<user> getbyId(int id) {
        user user = userservice.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<user> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        user user = userservice.login(loginDTO);
        session.setAttribute("USER_KEY", user); // 键"USER_KEY"用于后续获取
        return Result.success(user);
    }

    @Transactional
    @AdminRequired
    @PostMapping("/batchDelete")
    public Result<Object> batchDelete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("你几个意思？");
        }
        boolean isSuccess = userservice.removeByIds(ids);
        if (isSuccess) {
            return Result.success("批量删除成功");
        }
        return Result.error("批量删除失败");
    }

    @Transactional
    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Integer userId
    ) {
        String newAvatarUrl = userservice.uploadAvatar(file, userId);
        return Result.success("上传成功", newAvatarUrl);
    }

}

//        UUID.randomUUID()：生成唯一文件名，避免上传同名图片覆盖；
//        file.transferTo(destFile)：把前端上传的图片保存到本地。
//          # 文件上传配置（新增，对应头像上传）
//        servlet:
//        multipart:
//        max-file-size: 10MB # 单个文件最大10MB（避免上传大头像报错）
//        max-request-size: 10MB # 单次请求最大10MB
//
//# 头像上传路径配置（核心，替换之前的properties配置）
//        upload:
//        avatar:
//    # Windows系统路径（直接复制用）
//        path: src/main/resources/static/uploads/avatar/
//    # 前端访问头像的前缀（不变）
//        url: /uploads/avatar/




