package com.ixinhoo.shiro;

import com.chunecai.crumbs.api.shiro.authority.ClassAndMethodAuthorizationAdvisor;
import com.google.common.collect.Maps;
import com.chunecai.crumbs.api.entity.ApiSetting;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.Map;


/**
 * shiro配置类
 *
 * @author cici
 */
@Configuration
public class ShiroConfiguration {


    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 设定Password校验的Hash算法与迭代次数;
     * 使用了ShiroRetryLimitMatcher 中的doCredentialsMatch进行密码验证;
     * 所以此项@PostConstruct 设置密码验证matcher去掉,在ShiroRetryLimitMatcher 中进行验证.
     * 如果不使用ShiroRetryLimitMatcher需要打开@PostConstruct;
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ApiSetting.HASH_ALGORITHM);
        matcher.setHashIterations(ApiSetting.HASH_INTERATIONS);
        return matcher;
    }

    /**
     * ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
     * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
     */
    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    /**
     * EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，
     * 然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
     */
    @Bean(name = "shiroEhCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager() {
        return new EhCacheManager();
    }

    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

       /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filters = Maps.newLinkedHashMap();
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/api/bg/login");
//        filters.put("logout",null);
        MobileAuthenticatingFilter mobile = new MobileAuthenticatingFilter();
        mobile.setName("mobile");
        filters.put("mobile", mobile);
        shiroFilterFactoryBean.setFilters(filters);

//        Map<String, String> filterChainDefinitionManager = Maps.newHashMap();
        Map<String, String> filterChainDefinitionManager = Maps.newLinkedHashMap();
        filterChainDefinitionManager.put("/logout", "logout");
        filterChainDefinitionManager.put("/api/bg/login", "anon");
        filterChainDefinitionManager.put("/api/anon/**", "anon");
//        filterChainDefinitionManager.put("/api/bg/**/list**", "roles[admin]");
//        filterChainDefinitionManager.put("/api/bg/**/save**", "roles[admin]");
//        filterChainDefinitionManager.put("/api/bg/**/delete", "roles[admin]");
//        filterChainDefinitionManager.put("/api/bg/**", "mobile");
//        filterChainDefinitionManager.put("/api/v1/**", "mobile");
//        filterChainDefinitionManager.put("/api/bg/login-validate", "mobile");
//        filterChainDefinitionManager.put("/api/bg/**", "mobile");
//        filterChainDefinitionManager.put("/**", "anon");
        filterChainDefinitionManager.put("/api/v1/subjects/stage", "anon");
        filterChainDefinitionManager.put("/api/v1/document/search", "anon");
        filterChainDefinitionManager.put("/api/v1/question/search", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/search-chapter", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/recommend", "anon");
        filterChainDefinitionManager.put("/api/v1/document/detail", "anon");
        filterChainDefinitionManager.put("/api/v1/document/guess", "anon");
        filterChainDefinitionManager.put("/api/v1/document/same-type", "anon");
        filterChainDefinitionManager.put("/api/v1/area/province", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/search-grade", "anon");
        filterChainDefinitionManager.put("/api/v1/question/paper", "anon");
        filterChainDefinitionManager.put("/api/v1/question/make-paper", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/paper-type", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/paper-template", "anon");
        filterChainDefinitionManager.put("/api/v1/papers/paper-analysis", "anon");
        filterChainDefinitionManager.put("/api/v1/question/paper", "anon");
        filterChainDefinitionManager.put("/api/v1/question/type-knowledge", "anon");
        filterChainDefinitionManager.put("/api/v1/user-paper-template/make-paper", "anon");

        filterChainDefinitionManager.put("/**", "mobile");
        filterChainDefinitionManager.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);


        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/api/bg/login-validate");
        return shiroFilterFactoryBean;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }

    @Bean
    public ClassAndMethodAuthorizationAdvisor classAndMethodAuthorizationAdvisor() {
        ClassAndMethodAuthorizationAdvisor aASA = new ClassAndMethodAuthorizationAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }


}

