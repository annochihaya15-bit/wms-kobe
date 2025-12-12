package org.example.springboot_wms.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springboot_wms.common.AdminRequired;
import org.example.springboot_wms.common.Result;
import org.example.springboot_wms.domain.user; // 这里保持小写，对应你的实体类名
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 权限拦截器
 * 只有拥有 @AdminRequired 注解的方法才会被拦截校验
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=== 进入拦截器 ===");
        System.out.println("当前请求路径: " + request.getRequestURI());
        AdminRequired adminRequired = null;
        System.out.println("是否获取到注解: " + (adminRequired != null));
        // 1. 如果不是映射到Controller方法的请求（例如静态资源），直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        user loginUser = (user) request.getSession().getAttribute("USER_KEY");
        if (loginUser == null) {
            return returnJson(response, "用户未登录，请先登录");
        }


        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 2. 检查方法上是否有 @AdminRequired 注解
        adminRequired = handlerMethod.getMethodAnnotation(AdminRequired.class);
        if (adminRequired == null) {
            return true; // 没有注解，说明该接口不需要管理员权限，放行
        }


        // 4. 校验权限
        // 假设你的数据库设计：0=超级管理员, 1=管理员, 2=普通用户
        // 注意：请确认你的 user 类中获取角色的方法名。
        // 如果字段是 roleid，方法通常是 getRoleid()
        // 如果字段是 roleId，方法通常是 getRoleId()
        Integer roleId = loginUser.getRoleid();

        // 防止 roleId 为 null 的空指针异常
        if (roleId == null) {
            return returnJson(response, "用户角色信息异常");
        }

        // 逻辑：如果是超级管理员(0) 或者 管理员(1)，则放行
        if (roleId == 0 || roleId == 1) {
            return true;
        }

        // 5. 权限不足（例如 roleId == 2）
        return returnJson(response, "无管理员权限，无法操作");
    }

    /**
     * 辅助方法：返回 JSON 格式的错误信息
     */
    private boolean returnJson(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        // 使用 Result.error 返回统一格式的错误
        String json = objectMapper.writeValueAsString(Result.error(msg));
        response.getWriter().write(json);
        return false; // 拦截请求
    }
}