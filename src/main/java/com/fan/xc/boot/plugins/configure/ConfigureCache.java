package com.fan.xc.boot.plugins.configure;

import com.fan.xc.boot.starter.utils.Conversion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义配置容器类
 * 可以通过覆写该类，将自定义配置缓存到更多的地方，比如redis
 * 通过给覆写类添加{@link Primary}注解来接替该类的工作
 * 或者通过设定{@link EnableCustomerConfig}的value来指定工作的缓存类
 * 覆写类必须为Map子类
 * @author fan
 * date 2020/1/8 10:38
 **/
@Slf4j
public class ConfigureCache extends HashMap<String, Object> implements InitializingBean, ApplicationContextAware {
    private static final long serialVersionUID = 2330239289603967978L;

    private ApplicationContext applicationContext;

    /**
     * 获取参数，并提供参数转换
     * @param key   待获取的KEY
     * @param clazz 需要转换的类型
     */
    public <T> T value(String key, Class<T> clazz) {
        Object v = get(key);
        if (v == null) {
            return null;
        }
        return Conversion.getBinder().convertIfNecessary(v, clazz);
    }

    /**
     * 执行自定义参数加载方法
     * date 2020/1/11 17:28
     * @author fan
     */
    @Override
    public void afterPropertiesSet() {
        Map<String, LoadConfig> beans = applicationContext.getBeansOfType(LoadConfig.class);
        ArrayList<LoadConfig> loadConfigs = new ArrayList<>(beans.values());
        // 实现Spring的排序
        AnnotationAwareOrderComparator.sort(loadConfigs);
        loadConfigs.forEach(it -> it.load(this));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
