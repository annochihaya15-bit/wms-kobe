package org.example.springboot_wms.common;

import lombok.Data;




@Data
public class BasePageParam {
   private int currentPage=1;
   private int pageSize=1;
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<?> toPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(currentPage, pageSize);
    }
}
