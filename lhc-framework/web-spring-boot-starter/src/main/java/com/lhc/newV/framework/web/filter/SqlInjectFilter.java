package com.lhc.newV.framework.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "SQLInjection")
public class SqlInjectFilter implements Filter {
    private String regx;
 
    @Override
    public void init(FilterConfig filterConfig) {
        this.regx = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" +
                "(\\b(and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|or|like'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|--|like)\\b)";
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Map<String, String[]> parametersMap = servletRequest.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parametersMap.entrySet()) {
            String[] value = (String[]) ((Map.Entry<?, ?>) stringEntry).getValue();
            for (String s : value) {
                if (null != s && this.regx != null) {
                    Pattern p = Pattern.compile(this.regx);
                    Matcher m = p.matcher(s);
                    if (m.find()) {
                        log.error("您输入的参数有非法字符，请输入正确的参数！");
                        servletRequest.setAttribute("err", "您输入的参数有非法字符，请输入正确的参数！");
                        servletRequest.setAttribute("pageUrl", req.getRequestURI());
                        servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
 
        @Override
        public void destroy() {

       }
}