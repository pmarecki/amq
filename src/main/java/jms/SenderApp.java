package jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableJms
@EnableWebMvc
@ImportResource("classpath:sender-receiver-jms.xml")
public class SenderApp {


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(SenderApp.class, args);

        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
//        jmsTemplate.setDeliveryPersistent(false);

//        MessageListenerContainer container = ctx.getBean("jmsContainer1", MessageListenerContainer.class);
//        container.stop();

//        jmsTemplate.send("box1", session -> session.createTextMessage("ping!"));
//        jmsTemplate.send("box1", session -> session.createTextMessage("ping!"));
//        jmsTemplate.send("box1", session -> session.createTextMessage("ping!"));
        for (int i = 0; i < 1000000; i++) {
            Integer ii = i;
            if (i%100==0) System.out.println(i);
            jmsTemplate.send("box1", session -> {
                TextMessage m = session.createTextMessage();
                m.setJMSCorrelationID("000" + ii);
                m.setJMSReplyTo(jmsTemplate.getDefaultDestination());
                m.setJMSPriority(9);
                return m;
            });
        }
        Thread.sleep(1000);
        jmsTemplate.convertAndSend("box1", Arrays.asList(1,2,3));
        ctx.close();

    }

}