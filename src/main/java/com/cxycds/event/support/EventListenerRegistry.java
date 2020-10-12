package com.cxycds.event.support;

import com.cxycds.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by leicheng on 2020/10/10.
 */

@Component
public class EventListenerRegistry implements BeanFactoryPostProcessor {
    private final static Logger logger = LoggerFactory.getLogger(EventListenerRegistry.class);
    private static final Pattern skipPackagesPattern = Pattern.compile("^org\\.apache\\..*|^org\\.hibernate\\..*|^org\\.springframework\\..*");
    private Map<String, Map<String, List<Method>>> registry = new HashMap<>(8);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        long start = System.currentTimeMillis();
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            BeanDefinition beanDefinition;
            try {
                beanDefinition = beanFactory.getBeanDefinition(beanName);
            } catch (NoSuchBeanDefinitionException e) {
                continue;
            }
            String beanClassName = beanDefinition.getBeanClassName();
            if (StringUtils.isEmpty(beanClassName) || skipPackagesPattern.matcher(beanClassName).matches()) {
                continue;
            }
            Class beanClass;
            try {
                beanClass = Class.forName(beanFactory.getBeanDefinition(beanName).getBeanClassName());
            } catch (ClassNotFoundException var23) {
                throw new RuntimeException(var23);
            }

            Class targetClass = beanClass;
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method : methods) {
                EventListener eventListener = method.getAnnotation(EventListener.class);
                if (eventListener == null) {
                    continue;
                }
                Assert.isTrue(!StringUtils.isEmpty(eventListener.name()), "监听器的name不能为空");
                String outerKey = eventListener.type().getSimpleName();
                Map<String, List<Method>> methodHashMap = registry.getOrDefault(outerKey, new HashMap<>());
                if (methodHashMap.containsKey(eventListener.name())) {
                    List<Method> methodList = methodHashMap.get(eventListener.name());
                    for (int j = 0; j < methodList.size(); j++) {
                        Method current = methodList.get(j);
                        if (current.getAnnotation(EventListener.class).order() > method.getAnnotation(EventListener.class).order()) {
                            methodList.add(j, method);
                        }
                    }
                    methodHashMap.put(eventListener.name(), methodList);
                } else {
                    ArrayList<Method> list = new ArrayList<>(2);
                    list.add(method);
                    methodHashMap.put(eventListener.name(), list);
                }
                registry.put(outerKey, methodHashMap);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("获取所有的监听器完成,size:{},cost:{}ms", registry.size(), (System.currentTimeMillis() - start));
        }
    }

    public Map<String, List<Method>> getListeners(Class<?> typeClazz) {
        return registry.getOrDefault(typeClazz.getSimpleName(), Collections.emptyMap());
    }

    public List<Method> getListeners(Class<?> typeClazz, String name) {
        return registry.getOrDefault(typeClazz.getSimpleName(), Collections.emptyMap()).get(name);
    }

    public Map<String,Map<String,List<Method>>> getRegistry(){
        return registry;
    }
}
