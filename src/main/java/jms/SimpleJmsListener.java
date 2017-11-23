package jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SimpleJmsListener implements MessageListener, InitializingBean {
    AtomicInteger cnt = new AtomicInteger();

    @Override
    public void onMessage(Message message) {
        int x = cnt.incrementAndGet();
        System.out.println(".");
        if (x%1000==0) {
            System.out.println(x);
        }
        try {
//            throw new IllegalArgumentException("aaa");
            TextMessage tm = (TextMessage)message;
            String content = tm.getText();
            log.warn("Received message [{}]", content);
//            log.info("Msg [{}] timestamp: [{}], reply-to: [{}], reply-dest: [{}], delivery-mode: [{}], " +
//                            "correlation: [{}], expiriation: [{}], priority: [{}], JMSType: [{}] ",
//                    content,
//                    Instant.ofEpochMilli(tm.getJMSTimestamp()),
//                    tm.getJMSReplyTo(),
//                    tm.getJMSDestination(),
//                    tm.getJMSDeliveryMode(),
//                    tm.getJMSCorrelationID(),
//                    tm.getJMSExpiration(),
//                    tm.getJMSPriority(),
//                    tm.getJMSType()
//            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("############## Initializing JMS listener ##############");
    }
}
