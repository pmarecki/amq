package jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReceiverApp {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("receiver-jms.xml");
        Thread.sleep(1000000);
        ctx.close();
    }
}