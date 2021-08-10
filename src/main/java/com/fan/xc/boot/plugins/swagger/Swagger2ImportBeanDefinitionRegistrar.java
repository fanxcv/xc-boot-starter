package com.fan.xc.boot.plugins.swagger;

import com.fan.xc.boot.starter.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author fan
 */
@Slf4j
public class Swagger2ImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    protected static String packageName = Dict.BLANK;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata annotationMetadata,
                                        @NonNull BeanDefinitionRegistry registry) {
        Optional.ofNullable(annotationMetadata.getAnnotationAttributes(EnableXcSwagger.class.getName()))
                .ifPresent(it -> {
                    final String packageName = (String) it.get("value");
                    if (StringUtils.hasText(packageName) && StringUtils.hasText(packageName)) {
                        log.debug("==> swagger2 base package is: {}", it);
                        Swagger2ImportBeanDefinitionRegistrar.packageName = packageName;
                    }
                });
        final Class<Swagger2Config> clazz = Swagger2Config.class;
        final RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
        registry.registerBeanDefinition(clazz.getName(), beanDefinition);
    }
}
