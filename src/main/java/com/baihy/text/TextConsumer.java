package com.baihy.text;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @projectName: activemq
 * @packageName: com.baihy.text
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/16 11:13
 */
public class TextConsumer {

    public String consumerTextMessage() {

        String returnCode = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageConsumer consumer = null;
        Message message = null;
        try {
            factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.21.21:61616");
            connection = factory.createConnection();
            /**
             * 消息的消费者必须启动连接，否则无法处理消息
             */
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("first-queue");
            /**
             *创建消费者对象，在指定的目的地中获取消息
             */
            consumer = session.createConsumer(destination);
            /**
             * 获取队列中的消息，receive方法是一个主动获取的方法。执行一次拉取一个消息。
             */
            // consumer的receive方法中的超时是consumer连接activemq服务器超时，而不是消息确认超时。是等待多久后，consumer没有消息可处理，超时。
            // consumer.receive(1000);
            message = consumer.receive();

            TextMessage textMessage = (TextMessage) message;
            returnCode = textMessage.getText();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnCode;
    }
}
