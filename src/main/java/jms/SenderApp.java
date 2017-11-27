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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

class MyMessageCreator implements MessageCreator {
    AtomicInteger counter = new AtomicInteger();

    @Override
    public Message createMessage(Session session) throws JMSException {
        TextMessage m = session.createTextMessage("SimpleMessage" + counter.incrementAndGet());
        m.setJMSCorrelationID("000" + counter.get());

//        m.setJMSReplyTo(jmsTemplate.getDefaultDestination());
        m.setJMSPriority(9);
        return m;
    }
}

@SpringBootApplication
@EnableJms
@EnableWebMvc
@Slf4j
@ImportResource("classpath:sender-jms.xml")
public class SenderApp {


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(SenderApp.class, args);

        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
        jmsTemplate.getSessionAcknowledgeMode();
        jmsTemplate.setDeliveryPersistent(false);

        MessageCreator mc = new MyMessageCreator();
        for(;;) {
            Scanner s = new Scanner(System.in);
            int msgCount = s.nextInt();
            if (msgCount==0) break;

            for (int i = 0; i < msgCount; i++) {
                if (i%100==0) System.out.println(i);
                jmsTemplate.send("xxx_topic", mc);
            }

        }

        ctx.close();

    }

}
