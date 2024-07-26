package com.zhaoyss.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

/**
 * 实现多语言，要在View种使用MessageSource加上Locale输出多语言，我们通过编写一个MvcInterceptor把相关资源注入到ModelAndView中。
 */
@Component
public class MvcInterceptor {

    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    @Qualifier("i18n")
    MessageSource messageSource;

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        if (modelAndView != null
        && modelAndView.getViewName() != null
        && !modelAndView.getViewName().startsWith("redirect:")){
            // 解析用户的Locale
            Locale locale = localeResolver.resolveLocale(request);
            // 放入 Model
            modelAndView.addObject("__messageSource__",messageSource);
            modelAndView.addObject("__locale__",locale);
        }
    }
}
