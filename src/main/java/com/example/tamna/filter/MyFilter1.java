package com.example.tamna.filter;


import javax.servlet.*;
import java.io.IOException;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("필터1");
//        PrintWriter out = response.getWriter();
//        out.print("안녕");  프로세스 진행 안되고 끝남
        chain.doFilter(request, response); // 프로세스를 계속 진행하기 위해선 체인에 걸어줘야 함
    }
}
