package com.codestates.filter;

import javax.servlet.*;
import java.io.IOException;

public class FirstFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("=== First 필터 생성 ===");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("=== First 필터 시작 ===");

        chain.doFilter(request, response);

        System.out.println("=== First 필터 시작 ===");
    }

    @Override
    public void destroy() {
        System.out.println("=== First 필터 파괴 ===");
        Filter.super.destroy();
    }

}
