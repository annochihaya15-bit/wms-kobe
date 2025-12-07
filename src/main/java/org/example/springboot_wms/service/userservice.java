package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.springboot_wms.domain.user;

import java.util.List;

public interface userservice extends IService<user> {
    List<user> listAll();
}
