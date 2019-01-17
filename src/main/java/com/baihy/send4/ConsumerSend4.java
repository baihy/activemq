package com.baihy.send4;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @projectName: activemq
 * @packageName: com.baihy.send4
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/17 11:45
 */
public class ConsumerSend4 {

    /**
     * 处理消息
     */
    public void consumMessage() {

        ConnectionFactory factory = null;

        Connection connection = null;

        Session session = null;

        Destination destination = null;

        MessageConsumer consumer = null;

        try {
            factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.21.21:61616");
            connection = factory.createConnection();
            connection.start();
            connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("test-send-params");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if(message instanceof  ObjectMessage){
                        ObjectMessage objectMessage = (ObjectMessage) message;
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
