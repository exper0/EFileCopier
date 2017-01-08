package com.github.exper0.efilecopier.ftp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.PollableChannel;

import java.io.IOException;

/**
 * Created by Acer-V3 on 06.01.2017.
 */
public class FtpAdapter implements FileAdapter {
    private PollableChannel channel;
    private ConfigurableApplicationContext ctx;
    FtpAdapter(ConfigurableApplicationContext ctx) {
        this.ctx = ctx;
        this.channel = ctx.getBean("ftpChannel", PollableChannel.class);
    }

    @Override
    public PollableChannel channel() {
        return channel;
    }

    @Override
    public void activate() {
        ctx.refresh();
    }

    @Override
    public void close() throws IOException {
        ctx.stop();
    }
}
