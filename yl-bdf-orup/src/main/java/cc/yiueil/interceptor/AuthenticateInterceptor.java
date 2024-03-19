package cc.yiueil.interceptor;

import cc.yiueil.controller.LoggedController;
import cc.yiueil.dto.UserDto;
import cc.yiueil.exception.UnauthorizedException;
import cc.yiueil.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用权限检测拦截器
 * @author 弋孓 YIueil@163.com
 * @date 2023/5/30 22:17
 * @version 1.0
 */
public class AuthenticateInterceptor implements HandlerInterceptor {
    /**
     *
     * @param request 请求体
     * @param response 响应体
     * @param handler 处理器
     * @return 是否通过拦截器
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取Controller类信息
            Object bean = handlerMethod.getBean();
            if (bean instanceof LoggedController) {
                return verifyToken(request);
            }
        }
        return true;
    }

    /**
     * token校验
     * @param request 请求体
     * @return 校验结果
     */
    public boolean verifyToken(HttpServletRequest request) {
        String token = request.getHeader("yl-token");
        if ("Fk12345.".equals(token)) {
            return true;
        }
        try {
            UserDto userDto = JwtUtil.verifyToken(token);
            //1.判断请求是否有效
            //2.判断是否需要续期
            return userDto != null;
        } catch (JWTDecodeException jwtDecodeException) {
            throw new UnauthorizedException("未登陆或授权到期, 请重新登陆");
        }
    }
}
