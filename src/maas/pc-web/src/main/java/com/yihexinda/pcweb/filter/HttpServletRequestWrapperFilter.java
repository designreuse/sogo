package com.yihexinda.pcweb.filter;

import com.yihexinda.pcweb.interceptor.MyRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/4 0004
 */
@WebFilter(filterName="HttpServletRequestWrapperFilter",urlPatterns="/api/*")
public class HttpServletRequestWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper= null;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //filterChain.doFilter(request,response);
        if(request instanceof HttpServletRequest){
            requestWrapper = new MyRequestWrapper((HttpServletRequest) request);
       }
       if(null == requestWrapper){
           filterChain.doFilter(request,servletResponse);
        }else{
            filterChain.doFilter(requestWrapper,servletResponse);
       }
    }

    @Override
    public void destroy() {

    }
}
