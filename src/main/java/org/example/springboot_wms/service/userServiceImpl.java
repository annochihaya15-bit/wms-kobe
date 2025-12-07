package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot_wms.domain.user;
import org.example.springboot_wms.mapper.usermapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userServiceImpl extends ServiceImpl<usermapper, user> implements userservice {
@Resource
    private usermapper usermapper;
    @Override
    public List<user> listAll() {
        return usermapper.listAll();
    }
}
