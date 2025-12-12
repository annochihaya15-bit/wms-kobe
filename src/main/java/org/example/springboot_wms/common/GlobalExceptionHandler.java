package org.example.springboot_wms.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 1. @RestControllerAdvice
// 这个注解的意思是：我是所有 Controller 的"建议者" (Advice)。
// 只要任何 Controller 出了事，都要先问问我。
// 它还自带了 @ResponseBody，保证返回给前端的是 JSON 数据。
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 3. @ExceptionHandler
// 括号里的 Exception.class 是 Java 里所有异常的"老祖宗"。
// 意思是：不管你是空指针、数组越界、还是算术错误，只要是异常，全都被这个方法捕获。
    @ExceptionHandler(Exception.class)
    public  Result<String> myHandle(Exception e){
        // 4.1 做笔录 (给后端看)
        // 这一行非常重要！如果不写，后台一片寂静，你都不知道哪里出 Bug 了。
        e.printStackTrace();
        return Result.error(500, "服务器错误"+e.getMessage());

    }

}
