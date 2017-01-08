/*
 * Copyright 2002-2016 the original author or authors.
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.exper0.efilecopier.ftp.TestSuite.LOCAL_FTP_TEMP_DIR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 * @author Andrei Eliseev (aeg.exper0@gmail.com)
 *
 */
public class FtpInboundChannelAdapterSample {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpInboundChannelAdapterSample.class);

	@Test
	public void runDemo() throws Exception{
//		ConfigurableApplicationContext ctx =
//			new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpInboundChannelAdapterSample-context.xml");
//
        try (FtpAdapterFactory resolver = new FtpAdapterFactory()) {
            Files.copy(this.getClass().getResourceAsStream("/test-files/a.txt"), Paths.get(TestSuite.FTP_ROOT_DIR, "a.txt"));
            Files.copy(this.getClass().getResourceAsStream("/test-files/b.txt"), Paths.get(TestSuite.FTP_ROOT_DIR, "b.txt"));
            ReportSettings settings = new ReportSettings();
            settings.setPort(4444);
            settings.setHost("localhost");
            settings.setLocalDir(LOCAL_FTP_TEMP_DIR + "/ftpInbound");
            settings.setRemoteDir("/");
            settings.setUser("demo");
            settings.setPassword("demo");
            PollableChannel ftpChannel = resolver.resolve(settings);


            Message<?> message1 = ftpChannel.receive(2000);
            Message<?> message2 = ftpChannel.receive(2000);
            Message<?> message3 = ftpChannel.receive(1000);

            LOGGER.info(String.format("Received first file message: %s.", message1));
            LOGGER.info(String.format("Received second file message: %s.", message2));
            LOGGER.info(String.format("Received nothing else: %s.", message3));

            assertNotNull(message1);
            assertNotNull(message2);
            assertNull("Was NOT expecting a third message.", message3);
        }
	}

}
