package com.baihy.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @projectName: activemq
 * @packageName: com.baihy.topic
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/16 15:21
 */
public class TopicProducer {

    public void sendMessage(String data) {

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
            producer = session.createProducer(null);
            message = session.createTextMessage(data);
            destination = session.createTopic("first-topic");
            producer.send(destination, message);
            System.out.println("消息已发送");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (producer != null) {
                try {
                    producer.close();
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
    }

    public static void main(String[] args) {
        TopicProducer producer = new TopicProducer();
        producer.sendMessage("topic Message");
    }

}
