package org.example.springboot_wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.springboot_wms.common.GoodsRecordQueryDTO;
import org.example.springboot_wms.common.Result;
import org.example.springboot_wms.domain.GoodsRecord;
import org.example.springboot_wms.service.goodsRecordService;
import org.example.springboot_wms.service.goodsservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class GoodsRecordController {
    @Autowired
    private goodsRecordService goodsRecordService;
    @Autowired
    private goodsservice goodsservice;

    @PostMapping("/listPage")
    public Result<Page<GoodsRecord>> listpage(@RequestBody GoodsRecordQueryDTO goodsRecordQueryDTO){
        Page<GoodsRecord> page = new Page<>(goodsRecordQueryDTO.getCurrentPage(), goodsRecordQueryDTO.getPageSize());
        LambdaQueryWrapper<GoodsRecord> wrapper = new LambdaQueryWrapper<>();//分页查询语句前置，抄过去就行了,记得改Goods为实体类名
        if(goodsRecordQueryDTO.getName() != null){
            wrapper.like(GoodsRecord::getGoodsName, goodsRecordQueryDTO.getName());
        }
        wrapper.orderByDesc(GoodsRecord::getCreatetime);
        Page<GoodsRecord> resultPage = goodsRecordService.page(page, wrapper);// 3. 执行分页查询
        return Result.success(resultPage);
    }
}
