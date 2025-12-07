package org.example.springboot_wms.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("record")
public class GoodsRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "goods_name")
    private String goodsName;
    private Integer count;
    private String action;     // "入库" 或 "出库"
    @TableField(value = "operator")
    private String adminName;// 谁操作的
@TableField(value = "create_time")
    private LocalDateTime createtime;
@TableLogic
    private Integer deleted;
}

