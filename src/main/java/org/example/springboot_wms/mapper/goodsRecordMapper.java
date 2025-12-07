package org.example.springboot_wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.springboot_wms.domain.GoodsRecord;

@Mapper
public interface goodsRecordMapper extends BaseMapper<GoodsRecord> {
}
