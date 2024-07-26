package com.zhaoyss.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SyncFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("start SyncFilter...");
        chain.doFilter(request, response);
        logger.info("end SyncFilter.");    }
}
