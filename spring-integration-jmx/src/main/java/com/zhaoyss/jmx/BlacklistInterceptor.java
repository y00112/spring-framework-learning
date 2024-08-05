package com.zhaoyss.jmx;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(1)
public class BlacklistInterceptor implements HandlerInterceptor {

    @Autowired
    BlacklistMBean blacklistMBean;

    final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        logger.info("check ip address {} ...", ip);
        if (blacklistMBean.shouldBlock(ip)) {
            logger.warn("will block ip {} for it is in blacklist.", ip);
            response.sendError(403);
            return false;
        }
        return true;
    }
}
