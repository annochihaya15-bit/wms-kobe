package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.example.springboot_wms.common.LoginDTO;
import org.example.springboot_wms.domain.user;
import org.example.springboot_wms.mapper.usermapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class userServiceImpl extends ServiceImpl<usermapper, user> implements userservice {
    @Resource
    private usermapper usermapper;

    @Override
    public List<user> listAll() {
        return usermapper.listAll();
    }
    @Value("${upload.avatar.path}")
    private String avatarUploadPath;

    @Value("${upload.avatar.url}")
    private String avatarUrlPrefix;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moduser(user user, HttpSession session) {
        user USER_KEY = (user) session.getAttribute("USER_KEY");
        int loginid = USER_KEY.getRoleid();
        int targetid = user.getRoleid();
        if (USER_KEY == null) {
            throw new RuntimeException("登录已过期，请重新登录");
        }
        if (loginid != targetid&&loginid!=0) { // 这里根据你的业务规则写判断（比如“只能修改自己的信息”）
            throw new RuntimeException("id不符，无法执行修改操作"); // 或自定义异常类
        }
        this.updateById( user);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(int id,HttpSession session,user user){
        user USER_KEY = (user) session.getAttribute("USER_KEY");
        int loginid = USER_KEY.getRoleid();
        int targetid = user.getRoleid();
        if (loginid != targetid&&loginid!=0){
            throw new RuntimeException("id不符，无法执行删除操作");
        }
        this.removeById(id);
    }
    @Override
    public user login(LoginDTO loginDTO){
        LambdaQueryWrapper<user> wrapper = new LambdaQueryWrapper<>();//查询语句前置
        wrapper.eq(user::getName, loginDTO.getUsername());
        user user = this.getOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        boolean passeordMatch = user.getPassword().equals(loginDTO.getPassword());//匹配密码
        if (!passeordMatch) {
            throw new RuntimeException("密码错误");
        }
        user.setPassword(null);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadAvatar(MultipartFile file, Integer userId) {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 2. 准备路径 (复用你之前的逻辑)
        File uploadDir = new File(avatarUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 3. 生成文件名
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + suffix;

        try {
            // 4. 保存文件到磁盘
            file.transferTo(new File(avatarUploadPath + newFileName));

            // 5. 更新数据库
            String avatarUrl = avatarUrlPrefix + newFileName;
            user user = this.getById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            user.setAvatar(avatarUrl);
            this.updateById(user);

            return avatarUrl;

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

}