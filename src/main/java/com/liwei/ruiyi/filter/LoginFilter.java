package com.liwei.ruiyi.filter;

import com.liwei.ruiyi.utils.ReadProUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.getSession().setAttribute("basePath", ReadProUtils.ReadProperties("basePath"));
        String url = request.getRequestURI();
        if (isUrlAllowed(url) || isUserLoggedIn(request) || url.startsWith("/static/")) {
            filterChain.doFilter(request, response);
        } else {
            //Service Remote Access Port
            System.out.println("验证未通过url："+url);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    private boolean isUrlAllowed(String url) {
        String regex = "^/(static|login|index\\.jsp)(/|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return StringUtils.isNotEmpty(url) && (url.length() == 1 || matcher.find());
    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        // 这里实现判断用户是否登录的逻辑，例如检查会话中是否有特定的用户标识
        return request.getSession().getAttribute("admin") != null;
    }

    @Override
    public void destroy() {
    }
}
