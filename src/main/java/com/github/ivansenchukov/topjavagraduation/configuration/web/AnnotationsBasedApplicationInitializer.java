package com.github.ivansenchukov.topjavagraduation.configuration.web;

import com.github.ivansenchukov.topjavagraduation.configuration.DbConfig;
import com.github.ivansenchukov.topjavagraduation.configuration.RootApplicationConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AnnotationsBasedApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootApplicationConfig.class, DbConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
