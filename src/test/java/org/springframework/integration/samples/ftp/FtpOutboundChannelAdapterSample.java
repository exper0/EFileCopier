/*
 * Copyright 2002-2014 the original author or authors.
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
package org.springframework.integration.samples.ftp;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

/**
 * @author Gary Russell
 * @author Amol Nayak
 *
 */
public class FtpOutboundChannelAdapterSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpOutboundChannelAdapterSample.class);

    private final File baseFolder = new File("target" + File.separator + "toSend");

    @Test
    public void runDemo1() throws Exception{

//        ConfigurableApplicationContext ctx =
//            new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpOutboundChannelAdapterSample-context.xml");
//
//        MessageChannel ftpChannel = ctx.getBean("ftpChannel", MessageChannel.class);

        ConfigurableApplicationContext ctx =
            new ClassPathXmlApplicationContext("META-INF/spring/integration/DynamicFtpOutboundChannelAdapterSample-context.xml");
        MessageChannel ftpChannel = ctx.getBean("toDynRouter", MessageChannel.class);

        baseFolder.mkdirs();

        final File fileToSendA = new File(baseFolder, "a.txt");
        final File fileToSendB = new File(baseFolder, "b.txt");

        final InputStream inputStreamA = FtpOutboundChannelAdapterSample.class.getResourceAsStream("/test-files/a.txt");
        final InputStream inputStreamB = FtpOutboundChannelAdapterSample.class.getResourceAsStream("/test-files/b.txt");

        FileUtils.copyInputStreamToFile(inputStreamA, fileToSendA);
        FileUtils.copyInputStreamToFile(inputStreamB, fileToSendB);

        assertTrue(fileToSendA.exists());
        assertTrue(fileToSendB.exists());

        final Message<File> messageA = MessageBuilder.withPayload(fileToSendA)
                                        .setHeader("customer", "cust1")
                                        .build();
        final Message<File> messageB = MessageBuilder.withPayload(fileToSendB)
                                        .setHeader("customer", "cust1")
                                        .build();
        ftpChannel.send(messageA);
        ftpChannel.send(messageB);

        Thread.sleep(2000);

        assertTrue(new File(TestSuite.FTP_ROOT_DIR + File.separator + "a.txt").exists());
        assertTrue(new File(TestSuite.FTP_ROOT_DIR + File.separator + "b.txt").exists());

        LOGGER.info("Successfully transfered file 'a.txt' and 'b.txt' to a remote FTP location.");
        ctx.close();
    }

    @After
    public void cleanup() {
        FileUtils.deleteQuietly(baseFolder);
    }


/*
	@Test
	public void runDemo() throws Exception{
		ConfigurableApplicationContext ctx =
			new ClassPathXmlApplicationContext("META-INF/spring/integration/DynamicFtpOutboundChannelAdapterSample-context.xml");
		MessageChannel channel = ctx.getBean("toDynRouter", MessageChannel.class);




		File file = File.createTempFile("temp", "txt");
		Message<File> message = MessageBuilder.withPayload(file)
						.setHeader("customer", "cust1")
						.build();
		try {
			channel.send(message);
		}
		catch (MessagingException e) {
			assertThat(e.getCause().getCause().getCause(), instanceOf(UnknownHostException.class));
			assertTrue(e.getCause().getCause().getCause().getMessage().startsWith("host.for.cust1"));
		}
		// send another so we can see in the log we don't create the ac again.
		try {
			channel.send(message);
		}
		catch (MessagingException e) {
			assertThat(e.getCause().getCause().getCause(), instanceOf(UnknownHostException.class));
			assertTrue(e.getCause().getCause().getCause().getMessage().startsWith("host.for.cust1"));
		}
		// send to a different customer; again, check the log to see a new ac is built
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust2").build();
		try {
			channel.send(message);
		}
		catch (MessagingException e) {
			assertThat(e.getCause().getCause().getCause(), instanceOf(UnknownHostException.class));
			assertTrue(e.getCause().getCause().getCause().getMessage().startsWith("host.for.cust2"));
		}

		// send to a different customer; again, check the log to see a new ac is built
		//and the first one created (cust1) should be closed and removed as per the max cache size restriction
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust3").build();
		try {
			channel.send(message);
		}
		catch (MessagingException e) {
			assertThat(e.getCause().getCause().getCause(), instanceOf(UnknownHostException.class));
			assertTrue(e.getCause().getCause().getCause().getMessage().startsWith("host.for.cust3"));
		}

		//send to cust1 again, since this one has been invalidated before, we should
		//see a new ac created (with ac of cust2 destroyed and removed)
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust1").build();
		try {
			channel.send(message);
		}
		catch (MessagingException e) {
			assertThat(e.getCause().getCause().getCause(), instanceOf(UnknownHostException.class));
			assertEquals("host.for.cust1", e.getCause().getCause().getCause().getMessage());
		}

		ctx.close();
	}
*/
}
