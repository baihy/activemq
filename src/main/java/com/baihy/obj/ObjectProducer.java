package com.baihy.obj;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

/**
 * @projectName: activemq
 * @packageName: com.baihy.obj
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/16 14:16
 */
public class ObjectProducer {

    public void sendMessage() {
        ConnectionFactory factory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer producer = null;
        Message message = null;
        try {
            factory = new ActiveMQConnectionFactory("tcp://192.168.21.21:61616");
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            destination = session.createQueue("obj-listener");
            producer = session.createProducer(destination);
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                Integer obj = random.nextInt(100);
                message = session.createObjectMessage(obj);
                producer.send(message);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ObjectProducer producer = new ObjectProducer();
        producer.sendMessage();
    }

}
