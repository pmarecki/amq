package jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class MyMessageCreator implements MessageCreator {
    AtomicInteger counter = new AtomicInteger();

    @Override
    public Message createMessage(Session session) throws JMSException {
        TextMessage m = session.createTextMessage();
        m.setJMSCorrelationID("000" + counter.incrementAndGet());
//        m.setJMSReplyTo(jmsTemplate.getDefaultDestination());
        m.setJMSPriority(9);
        return m;
    }
}

@SpringBootApplication
@EnableJms
@EnableWebMvc
@Slf4j
@ImportResource("classpath:sender-receiver-jms.xml")
public class SenderApp {


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(SenderApp.class, args);

        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
        jmsTemplate.getSessionAcknowledgeMode();
        jmsTemplate.setDeliveryPersistent(false);

        MessageCreator mc = new MyMessageCreator();

        for (int i = 0; i < 1_000_000; i++) {
            if (i%100==0) System.out.println(i);
            jmsTemplate.send("xxx_topic", mc);
        }
        Thread.sleep(1000);
        ctx.close();

    }

}
