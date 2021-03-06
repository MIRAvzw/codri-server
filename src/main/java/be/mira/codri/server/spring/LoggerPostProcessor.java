/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.spring;

/**
 * Auto injects the underlying implementation of logger into the bean with field
 * having annotation
 * <code>Logger</code>.
 *
 */
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import static org.springframework.util.ReflectionUtils.FieldCallback;

public class LoggerPostProcessor implements BeanPostProcessor {
    //
    // BeanPostProcessor interface
    //

    @Override
    public final Object postProcessAfterInitialization(final Object iBean, final String iBeanName) {
        return iBean;
    }

    @Override
    public final Object postProcessBeforeInitialization(final Object iBean, final String iBeanName) {
        ReflectionUtils.doWithFields(iBean.getClass(), new FieldCallback() {
            @Override
            public final void doWith(final Field iField) throws IllegalAccessException {
                // Make the field accessible if defined private  
                ReflectionUtils.makeAccessible(iField);
                if (iField.getAnnotation(Slf4jLogger.class) != null) {
                    Logger tLog = LoggerFactory.getLogger(iBean.getClass());
                    iField.set(iBean, tLog);
                }
            }
        });
        return iBean;
    }
}
