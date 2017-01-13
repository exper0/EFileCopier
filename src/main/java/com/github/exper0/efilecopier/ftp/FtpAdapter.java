package com.github.exper0.efilecopier.ftp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.messaging.PollableChannel;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Acer-V3 on 06.01.2017.
 */
public class FtpAdapter implements FileAdapter {
    private PollableChannel channel;
    private ConfigurableApplicationContext ctx;

    FtpAdapter(FtpReportSettings settings, AbstractApplicationContext parent, String factory) {
        this.ctx = new ClassPathXmlApplicationContext(
            new String[]{"/META-INF/spring/integration/ftp-inbound-adapter-template.xml"},
            false, parent);
        StandardEnvironment env = new StandardEnvironment();
        Properties props = new Properties();
        // populate properties for customer
        props.setProperty("remote.directory", settings.getRemoteDir());
        props.setProperty("local.directory", settings.getLocalDir());
        props.setProperty("session.factory", factory);
        PropertiesPropertySource pps = new PropertiesPropertySource("ftpprops", props);
        env.getPropertySources().addLast(pps);
        this.ctx.setEnvironment(env);
    }

    @Override
    public PollableChannel channel() {
        return this.channel;
    }

    @Override
    public void activate() {
        this.ctx.refresh();
        this.channel = ctx.getBean("ftpChannel", PollableChannel.class);
    }

    @Override
    public void close() throws IOException {
        this.ctx.stop();
    }

}
