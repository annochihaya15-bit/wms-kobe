package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot_wms.domain.Goods;
import org.example.springboot_wms.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

@Service
public class goodsServicelmpl extends ServiceImpl<GoodsMapper, Goods> implements goodsservice{

}
