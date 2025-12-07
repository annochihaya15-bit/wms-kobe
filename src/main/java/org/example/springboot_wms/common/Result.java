package org.example.springboot_wms.common;

import lombok.Data;

/**
 * 统一返回给前端的封装类
 * @param <T> 业务数据的类型（支持任意数据格式：List、Page、对象等）
 */
@Data
public class Result<T> {
    // 状态码：200=成功，500=服务器异常，400=参数错误等
    private Integer code;
    // 提示消息：成功/失败的描述
    private String msg;
    // 实际业务数据：分页结果、列表、对象等
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("请求成功");
        result.setData(data);
        return result;
    }
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
    public static <T> Result<T> success(String msg) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
