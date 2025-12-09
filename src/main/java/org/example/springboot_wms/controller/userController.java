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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


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

    @Transactional
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody user user) throws Throwable {
        try {
            boolean isSuccess = userservice.save(user);
            if (isSuccess) {
                return Result.success("保存用户成功");
            } else {
                return Result.error("保存用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志，方便排查
            throw Result.error(500, "保存失败：" + e.getMessage());
        }
    }

    @Transactional
    @AdminRequired
    @PostMapping("/mod")
    public Result<Object> mod(@RequestBody user user, HttpSession session) {
        user USER_KEY = (user) session.getAttribute("USER_KEY");
        int loginid = USER_KEY.getRoleid();
        int targetid = user.getRoleid();
        if (loginid == 2 && loginid != targetid) {
            return Result.error("请v我50解锁管理员权限");
        }
        boolean isSuccess = userservice.updateById(user);
        if (isSuccess) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    @Transactional
    @AdminRequired
    @GetMapping("/delete")
    public Object delete(int id) {
        boolean isSuccess = userservice.removeById(id);
        if (isSuccess) {
            return Result.success("删除用户成功");
        } else {
            return Result.error("删除用户失败");
        }
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
        LambdaQueryWrapper<user> wrapper = new LambdaQueryWrapper<>();//查询语句前置
        wrapper.eq(user::getName, loginDTO.getUsername());
        user user = userservice.getOne(wrapper);
        if (user == null) {
            return Result.error("用户不存在");
        }
        boolean passeordMatch = user.getPassword().equals(loginDTO.getPassword());//匹配密码
        if (!passeordMatch) {
            return Result.error("密码错误");
        }
        user.setPassword(null);
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
    @AdminRequired
    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Integer userId
    ) {
        // 1. 基础校验（保留）
        if (file.isEmpty()) return Result.error("请选择要上传的头像");
        user user = userservice.getById(userId);
        if (user == null) return Result.error("用户不存在");

        // 2. 读取配置的绝对路径（无需动态拼接，直接用）
        File uploadDir = new File(avatarUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 自动创建（保险，手动建了也没关系）
        }

        // 3. 文件名+保存（原有逻辑不变）
        String originalFileName = file.getOriginalFilename();
        String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileSuffix;

        try {
            File destFile = new File(avatarUploadPath + newFileName);
            file.transferTo(destFile); // 保存到D:/avatar_upload/

            // 4. 前端访问路径（不变，还是/uploads/avatar/xxx.jpg）
            String avatarUrl = avatarUrlPrefix + newFileName;
            user.setAvatar(avatarUrl);
            userservice.updateById(user);

            return Result.success("头像上传成功", avatarUrl);
        } catch (IOException e) {
            return Result.error("头像上传失败：" + e.getMessage());
        }
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




