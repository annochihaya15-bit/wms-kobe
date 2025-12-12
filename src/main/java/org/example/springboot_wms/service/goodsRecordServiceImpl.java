package org.example.springboot_wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot_wms.domain.GoodsRecord;
import org.example.springboot_wms.mapper.goodsRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class goodsRecordServiceImpl extends ServiceImpl<goodsRecordMapper,GoodsRecord> implements goodsRecordService {

}
