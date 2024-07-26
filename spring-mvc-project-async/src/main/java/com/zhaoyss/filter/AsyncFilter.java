package com.zhaoyss.filter;


import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AsyncFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("start AsyncFilter...");
        filterChain.doFilter(servletRequest,servletResponse);
        logger.info("end AsyncFilter...");
    }
}
