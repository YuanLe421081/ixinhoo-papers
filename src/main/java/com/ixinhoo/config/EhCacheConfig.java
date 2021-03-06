package com.ixinhoo.config;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * 缓存配置
 *
 * @author 448778074@qq.com (cici)
 */

@Configuration
@EnableCaching // 标注启动缓存
public class EhCacheConfig {
    /**
     * ehcache 主要的管理器
     *
     * @param bean
     * @return
     */
    @Bean
    @Primary //多个缓存的时候用到
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        return new EhCacheCacheManager(bean.getObject());
    }


    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();

        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);

        return factoryBean;
    }

}
