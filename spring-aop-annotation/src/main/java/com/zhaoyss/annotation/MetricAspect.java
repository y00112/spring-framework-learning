package com.zhaoyss.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {

    /**
     * 知识点：
     * @Around("@annotation(metricTime)")，他的意思是，符合条件的目标方法是带有 @MetricTime 注解的方法。
     * 引文 metric()方法参数类型是MetricTime
     * 有了@MetricTime注解，再配合MetricAspect，任何Bean，只要方法标注了@MetricTime注解，就可以自动实现性能监控。
     *
     * @param joinPoint
     * @param metricTime
     * @return
     */
    @Around("@annotation(metricTime)")
    public Object metric(ProceedingJoinPoint joinPoint,MetricTime metricTime){
        String name = metricTime.value();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }finally {
            long t = System.currentTimeMillis() - start;
            // 写入日志或发送至JMX:
            System.err.println("[Metrics] " + name + ": " + t + "ms");        }
    }
}
