/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.exper0.efilecopier.ftp;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrei Eliseev (aeg.exper0@gmail.com)
 * @version $Id$
 */
public class CachedAdapterFactory<T extends ReportSettings> implements FileAdapterFactory<T> {
    static final String FACTORY_NAME = "session.factory";
    private final Map<String, FtpAdapter> adapters = new HashMap<>();
    /**
     * Server to parent context map. Server is a host:port string.
     */
    private Map<String, AbstractApplicationContext> parents = new HashMap<String, AbstractApplicationContext>();
    private FileAdapterFactory<T> faсtory;

    public CachedAdapterFactory(FileAdapterFactory<T> factory) {
        this.faсtory = factory;
    }

    @Override
    public FileAdapter createAdapter(T settings) {
        return this.adapters.computeIfAbsent(
            settings.getReportName(),
            (k) -> new FtpAdapter(settings, getParent(settings), FACTORY_NAME)
        );
    }

    private AbstractApplicationContext getParent(ReportSettings settings) {
        final String key = String.format("%s:%d", settings.getHost(), settings.getPort());
        AbstractApplicationContext context = parents.get(key);
        if (context == null) {
            context = new GenericApplicationContext();
            DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
            factory.setUsername(settings.getUser());
            factory.setPassword(settings.getPassword());
            factory.setHost(settings.getHost());
            factory.setPort(settings.getPort());
            context.getBeanFactory().registerSingleton(FACTORY_NAME, factory);
            context.refresh();
            parents.put(key, context);
        }
        return context;
    }

}
