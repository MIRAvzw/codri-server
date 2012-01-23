/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.beans.factory;

/**
 * Auto injects the underlying implementation of logger into the bean with field
 * having annotation
 * <code>Logger</code>.
 *
 */
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import static org.springframework.util.ReflectionUtils.FieldCallback;

public class LoggerPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    public Object postProcessBeforeInitialization(final Object bean,
            String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                // make the field accessible if defined private  
                ReflectionUtils.makeAccessible(field);
                if (field.getAnnotation(Logger.class) != null) {
                    Log log = LogFactory.getLog(bean.getClass());
                    field.set(bean, log);
                }
            }
        });
        return bean;
    }
}
