package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot_wms.domain.Goods;
import org.example.springboot_wms.domain.GoodsRecord;
import org.example.springboot_wms.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class goodsServicelmpl extends ServiceImpl<GoodsMapper, Goods> implements goodsservice{
    @Autowired
    private goodsRecordService goodsRecordService;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGoodsRecord(Goods goods,String operator){
        GoodsRecord goodsRecord = new GoodsRecord();
        goodsRecord.setGoodsName(goods.getName());
        goodsRecord.setCreatetime(java.time.LocalDateTime.now());
        if (goods.getId() == null) {
            goodsRecord.setCount(goods.getCount());
            goodsRecord.setAction("初始化入库");
            goodsRecord.setAdminName(operator);
            goodsRecordService.save(goodsRecord);
        } else {
            // === 情况B：修改库存 ===
            // 关键点：我们要先查出数据库里"旧"的数据，跟前端传来的"新"数据比对
            Goods oldGoods = this.getById(goods.getId());//新数据
            int diffs = goods.getCount() - oldGoods.getCount();
            if (diffs != 0) {
                if (diffs > 0) {
                    goodsRecord.setAction("入库");
                    goodsRecord.setCount(Math.abs(diffs));//记录入库数量
                } else {
                    goodsRecord.setAction("出库");
                    goodsRecord.setCount(Math.abs(diffs));
                }
                goodsRecord.setAdminName(operator);
                goodsRecordService.save(goodsRecord);
            }
        }
        this.saveOrUpdate(goods);
    }
}
