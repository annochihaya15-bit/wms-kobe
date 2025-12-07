package org.example.springboot_wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.springboot_wms.domain.user;

import java.util.List;

@Mapper
public interface usermapper extends BaseMapper<user> {
    List<user> listAll();
}
