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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

/**
 * @author Andrei Eliseev (aeg.exper0@gmail.com)
 * @version $Id$
 */
public class FtpAdapterFactory implements FileAdapterFactory<FtpReportSettings>, Closeable {
    private static final String FACTORY_NAME = "session.factory";

    private final Map<String, FtpAdapter> adapters = new HashMap<>();
    /**
     * Server to parent context map. Server is a host:port string.
     */
    private Map<String, AbstractApplicationContext> parents = new HashMap<String, AbstractApplicationContext>();
    private Collection<ConfigurableApplicationContext> contexts = new ArrayList<>();

    @Override
    public FileAdapter create(FtpReportSettings settings) {
        FtpAdapter adapter = this.adapters.get(settings.getReportName());
        if (adapter == null) {
            adapter = createNewAdapter(settings);
        }
        return adapter;
    }

    private synchronized FtpAdapter createNewAdapter(FtpReportSettings settings) {
        FtpAdapter adapter = this.adapters.get(settings.getReportName());
        if (adapter == null) {
            ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"/META-INF/spring/integration/ftp-inbound-adapter-template.xml"},
                false, getContext(settings));
            this.setEnvironmentForReport(ctx, settings);
//            ctx.refresh();
            adapter = new FtpAdapter(ctx);
            this.adapters.put(settings.getReportName(), adapter);
            this.contexts.add(ctx);
        }
        return adapter;
    }

    /**
     * Use Spring 3.1. environment support to set properties for the
     * customer-specific application context.
     *
     * @param ctx
     * @param settings
     */
    private void setEnvironmentForReport(ConfigurableApplicationContext ctx,
                                         FtpReportSettings settings) {
        StandardEnvironment env = new StandardEnvironment();
        Properties props = new Properties();
        // populate properties for customer
        props.setProperty("remote.directory", settings.getRemoteDir());
        props.setProperty("local.directory", settings.getLocalDir());
        props.setProperty("session.factory", FACTORY_NAME);
        PropertiesPropertySource pps = new PropertiesPropertySource("ftpprops", props);
        env.getPropertySources().addLast(pps);
        ctx.setEnvironment(env);
    }

    private AbstractApplicationContext getContext(ReportSettings settings) {
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

    @Override
    public void close() throws IOException {
        for (ConfigurableApplicationContext context : contexts) {
            context.stop();
        }
    }
}
