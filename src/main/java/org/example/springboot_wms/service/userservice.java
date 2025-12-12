package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.example.springboot_wms.common.LoginDTO;
import org.example.springboot_wms.domain.user;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface userservice extends IService<user> {
    List<user> listAll();
    public void moduser(user user, HttpSession session);
    public void delete(int id,HttpSession  session, user  user);
    public user login(LoginDTO loginDTO);
    public String uploadAvatar(MultipartFile file, Integer userId);
}
