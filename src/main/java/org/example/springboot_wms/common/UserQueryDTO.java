package org.example.springboot_wms.common;

import lombok.Data;

@Data
public class UserQueryDTO extends  BasePageParam{
    private String name;
    private String password;
    private Integer minAge;
    private Integer maxAge;
    private Integer sex;
    private String phone;

}
