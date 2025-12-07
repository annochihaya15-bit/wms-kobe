package org.example.springboot_wms.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
@Data
public class Goods {
    @TableId(type = IdType.AUTO)
    @ExcelProperty
    private Integer id;
    @ExcelProperty("物品名称")
    private String name;
    @ExcelProperty("物品存放位置")
    private String storage;
    @ExcelProperty("物品类型")
    private String goodstype;
    @ExcelProperty("物品数量")
    private int count;
    @ExcelProperty("物品备注")
    private String remark;
    @TableLogic
    private Integer deleted;
}
