package com.baihy.obj;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @projectName: activemq
 * @packageName: com.baihy.text
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/16 13:59
 */
public class ObjectConsumerListener {

    public void consumerListener() {

        ConnectionFactory factory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageConsumer consumer = null;
        Message message = null;

        try {
            factory = new ActiveMQConnectionFactory("tcp://192.168.21.21:61616");
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("obj-listener");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                /**
                 * 	监听器一旦注册成功，永久生效（consumer线程不关闭）
                 * 	消息处理的方式：只要消息未处理，自动调用onMessage方法，处理消息。
                 * 	监听器可以注册若干个，注册多个监听器，相当于是集群。
                 * 	ActiveMQ自动的循环调用多个监听器，处理队列中的消息，实现并行处理
                 * 	处理消息的方法是监听方法。
                 * 	监听事件是：消息未处理
                 * 	要处理的具体内容：消息处理
                 * @param message
                 */
                @Override
                public void onMessage(Message message) {
                    if (message instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        try {
                            // acknowledge方法的作用就是消息的确认方法，代表customer已经接收消息，MQ删除对应的消息。
                            objectMessage.acknowledge();
                            Object obj = objectMessage.getObject();
                            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ":" + obj);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            try {
                // 阻塞当前代码，保证Listener代码不结束，如果代码结束了，那么监听器也就关闭了
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (consumer != null) {
                try {
                    connection.close();
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
        ObjectConsumerListener listener = new ObjectConsumerListener();
        listener.consumerListener();
    }

}
