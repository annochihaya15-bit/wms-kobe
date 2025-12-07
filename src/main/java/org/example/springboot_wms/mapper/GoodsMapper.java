package org.example.springboot_wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot_wms.domain.Goods;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    // 1. 统计所有物品的库存总数 (求和)
    // 逻辑：把 count 这一列全部加起来
    @Select("select sum(count) from goods")
//MyBatis 注解，允许你直接把 SQL 写在接口
    int selctTotalCount();

    // 2. 按分类统计库存 (分组查询)
    // 逻辑：按照 goodstype 分组，算出每一组的 sum(count)
    // 注意：这里返回 List<Map>，Map里包含两个字段：name(分类名), value(数量)
    // 这样前端 ECharts 拿到就能直接用，不用再转换了
    @Select("select goodstype as name,sum(count) as value from goods group by goodstype")
    List<Map<String, Object>> selectTotalCountByType();
//    使用Map<String, Object>来代表一行数据
}
