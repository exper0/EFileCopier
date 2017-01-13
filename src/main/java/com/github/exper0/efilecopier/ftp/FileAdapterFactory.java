package com.github.exper0.efilecopier.ftp;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Created by Acer-V3 on 06.01.2017.
 */
public interface FileAdapterFactory<T extends ReportSettings, S> {
    FileAdapter createAdapter(T settings, S sessionFactory);
    S createSessionFactory(T settings);

    default FileAdapter createAdapter(T settings) {
        return createAdapter(settings, createSessionFactory(settings));
    }

    default String sessionFactoryBeanName() {
        return "session.factory";
    }

    default AbstractApplicationContext createFactoryContext(S factory) {
        AbstractApplicationContext context = new GenericApplicationContext();
        context.getBeanFactory().registerSingleton(sessionFactoryBeanName(), factory);
        context.refresh();
        return context;
    }
}
