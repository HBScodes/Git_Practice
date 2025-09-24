package com.ruiji.filter;

import com.alibaba.fastjson.JSON;
import com.ruiji.common.BaseContext;
import com.ruiji.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的 URL
        String requestURL = request.getRequestURI();

        log.info("拦截到请求：{}",requestURL);

        //定义不需要处理的请求路径
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls,requestURL);

        //3、如果不需要处理，则直接放行
        if (check){
            log.info("本次请求{}不需要处理",requestURL);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户ID为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            //基于 ThreadLocal 封装工具类，用户保存和获取当前登录用户ID
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行（移动端）
        if (request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户ID为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            //基于 ThreadLocal 封装工具类，用户保存和获取当前登录用户ID
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流的方式向客户端相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURl
     * @return
     */
    public boolean check(String[] urls,String requestURl){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURl);
            if(match){
                return true;
            }
        }
        return false;
    }
    
}
