package com.github.ivansenchukov.topjavagraduation.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    @Autowired
    public CacheManager cacheManager(ApplicationContext appCtx) {
        JCacheManagerFactoryBean bean = new JCacheManagerFactoryBean();

        try {

            Resource ehcacheConfig = appCtx.getResource("classpath:ehcache.xml");
            bean.setCacheManagerUri(ehcacheConfig.getURI());

            bean.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JCacheCacheManager cm = new JCacheCacheManager();
        cm.setCacheManager(bean.getObject());

        return cm;
    }
}
