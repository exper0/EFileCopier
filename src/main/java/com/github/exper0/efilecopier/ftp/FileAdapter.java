package com.github.exper0.efilecopier.ftp;

import org.springframework.messaging.PollableChannel;

import java.io.Closeable;

/**
 * Created by Acer-V3 on 06.01.2017.
 */
public interface FileAdapter extends Closeable {
    PollableChannel channel();
    void activate();
}
