package org.example.springboot_wms.controller;
//controller打包方法返回值，并传递给前端

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.springboot_wms.common.GoodsQueryDTO;
import org.example.springboot_wms.common.Result;
import org.example.springboot_wms.domain.Goods;
import org.example.springboot_wms.domain.user;
import org.example.springboot_wms.mapper.GoodsMapper;
import org.example.springboot_wms.service.goodsRecordService;
import org.example.springboot_wms.service.goodsservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private goodsRecordService goodsRecordService;
    @Autowired
    private goodsservice goodsservice;
    @Autowired
    private GoodsMapper goodsMapper;

    @Transactional
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Goods goods, HttpSession session) {
        user Loginuser = (user) session.getAttribute("loginUser");
        String operator = Loginuser.getName();
        goodsservice.saveGoodsRecord(goods, operator);
        return Result.success("保存成功");
    }
    @Transactional
    @PostMapping("/delete")
    public Result<Boolean> delete(int id) {
        boolean issucesss = goodsservice.removeById(id);
        if (issucesss) {
            return Result.success("删除商品成功");
        } else {
            return Result.error("删除商品失败");
        }
    }

    @PostMapping("/listPage")
    public Result<Page<Goods>> listpage(@RequestBody GoodsQueryDTO goodsQueryDTO) {
        Page<Goods> page = new Page<>(goodsQueryDTO.getCurrentPage(), goodsQueryDTO.getPageSize());
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();//分页查询语句前置，抄过去就行了,记得改Goods为实体类名
        if (goodsQueryDTO.getName() != null) {
            wrapper.like(Goods::getName, goodsQueryDTO.getName());
        }
        Page<Goods> resultPage = goodsservice.page(page, wrapper);// 3. 执行分页查询
        return Result.success(resultPage);
    }

    //    后端去数据库把所有物品查出来，转换成 Excel 格式，然后通过网络“流”直接发给浏览器，让浏览器弹出一个下载框。
    @GetMapping("/export")
    public void exprot(HttpServletResponse response) throws IOException {
        // 1. 设置文件类型：告诉浏览器这是 Excel 文件 (.xlsx)
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置编码，防止乱码
        response.setCharacterEncoding("utf-8");
        // 处理文件名：因为文件名包含中文“物品库存表”，在网络传输中容易乱码，所以要用 URLEncoder 编码一下
        String fileName = URLEncoder.encode("物品库存表", "UTF-8").replaceAll("\\+", "%20");
        // 关键的一行！
// Content-Disposition: attachment 意思是“作为附件下载”，而不是在浏览器里直接打开
// filename=... 指定了下载下来的文件名
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<Goods> list = goodsservice.list();
        // EasyExcel.write(...) 开始写 Excel
// response.getOutputStream(): 这是通向浏览器的“管道”，数据写进去，浏览器就收到了
// Goods.class: 告诉 EasyExcel 用哪个实体类做模板（读取里面的 @ExcelProperty 注解来生成表头）
        EasyExcel.write(response.getOutputStream(), Goods.class)
                .sheet("库存数据") // Excel 下方那个 Sheet 页签的名字
                .doWrite(list);   // 把刚才查出来的 list 数据填进去
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> map = new HashMap<>();
        // 2. 查询总库存数
        Integer totalStock = goodsMapper.selctTotalCount();
// 防止数据库是空的返回 null，导致前端报错，做一个判断
        map.put("totalStock", totalStock == null ? 0 : totalStock);
        List<Map<String, Object>> categoryData = goodsMapper.selectTotalCountByType();
        map.put("categoryData", categoryData);
        return Result.success(map);
    }
//map.put(K, V)：
//    K (Key) = 数据的名字（前端要靠这个名字来取值）。
//    V (Value) = 数据的内容（具体的数字、字符串或列表）。
}
