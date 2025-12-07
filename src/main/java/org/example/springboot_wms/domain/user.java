package org.example.springboot_wms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
//@Data 映射数据到前端用的注解
public class user {
    @TableId(type = IdType.AUTO)//自增主键
    private Integer id;
    private String no;
    private String name;
    private String password;
    private int age;
    private int sex;
    private String phone;
    @TableField("role_id")
    private int roleid;
    @TableField("is_valid")
    private String isvalid;
    private String avatar;
    @TableLogic
    private int deleted;
}
