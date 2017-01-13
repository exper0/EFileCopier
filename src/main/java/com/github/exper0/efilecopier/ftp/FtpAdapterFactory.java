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

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

/**
 * @author Andrei Eliseev (aeg.exper0@gmail.com)
 * @version $Id$
 */
public class FtpAdapterFactory implements FileAdapterFactory<FtpReportSettings, DefaultFtpSessionFactory> {

    @Override
    public FileAdapter createAdapter(FtpReportSettings settings, DefaultFtpSessionFactory factory) {
        return new FtpAdapter(settings, createFactoryContext(factory), sessionFactoryBeanName());
    }

    @Override
    public DefaultFtpSessionFactory createSessionFactory(FtpReportSettings settings) {
        DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
        factory.setUsername(settings.getUser());
        factory.setPassword(settings.getPassword());
        factory.setHost(settings.getHost());
        factory.setPort(settings.getPort());
        return factory;
    }
}
