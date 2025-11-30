package com.getjob.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.util.JwtUtil;
import com.getjob.backend.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查类或方法是否有@RequireAuth注解
        RequireAuth requireAuth = handlerMethod.getMethodAnnotation(RequireAuth.class);
        if (requireAuth == null) {
            requireAuth = handlerMethod.getBeanType().getAnnotation(RequireAuth.class);
        }

        // 如果没有@RequireAuth注解，直接放行
        if (requireAuth == null) {
            return true;
        }

        // 从请求头获取token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendErrorResponse(response, ResultCode.UNAUTHORIZED.getCode(), "未授权，请先登录");
        }

        String token = authHeader.substring(7);

        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                return sendErrorResponse(response, ResultCode.UNAUTHORIZED.getCode(), "Token无效或已过期");
            }

            // 从token中提取用户信息并存储到ThreadLocal
            String userId = jwtUtil.getUserIdFromToken(token);
            String email = jwtUtil.getEmailFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            UserContext.setUserId(userId);
            UserContext.setEmail(email);
            UserContext.setUserType(userType);

            return true;
        } catch (Exception e) {
            return sendErrorResponse(response, ResultCode.UNAUTHORIZED.getCode(), "Token验证失败: " + e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

    private boolean sendErrorResponse(HttpServletResponse response, Integer code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", null);
        result.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }
}

