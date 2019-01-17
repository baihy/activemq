package com.baihy.send4;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.jms.*;
import java.io.Serializable;

/**
 * @projectName: activemq
 * @packageName: com.baihy.send4
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/17 10:40
 */
public class ProducerSend4 {

    private ConnectionFactory factory = null;

    private Connection connection = null;

    private Session session = null;

    private Destination destination = null;

    private MessageProducer producer = null;

    private Message message = null;

    /**
     * 直接发送消息
     *
     * @param obj
     */
    public void sendMessage(Serializable obj) {
        try {
            this.init("test-send");
            message = session.createObjectMessage(obj);
            producer.send(message);
            System.out.println("sendMessage method run");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定目的地发送消息
     *
     * @param obj             消息内容
     * @param destinationName 目的地名称
     */
    public void sendMessage4Destination(Serializable obj, String destinationName) {
        try {
            this.init();
            message = session.createObjectMessage(obj);
            destination = session.createQueue(destinationName);
            producer.send(destination, message);
            System.out.println("sendMessage4Destination method run");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多参数发送消息
     *
     * @param obj          消息内容
     * @param deliveryMode -持久化模式：
     *                     DeliveryMode.PERSISTENT:持久化; 消息会持久化到数据库（jdbc或kahadb）
     *                     DeliveryMode.NON_PERSISTENT:不持久化;不会持久化，只会保存在内存中
     * @param priority     -优先级：取值范围是0-9，取值越大优先级越高，但不保证绝对顺序。
     *                     必须要在activemq.xml配置文件的broker标签中增加配置
     * @param timeToLive   -消息的有效期时间，单位是毫秒。
     */
    public void sendMessageWithParameters(Serializable obj, int deliveryMode, int priority, int timeToLive) {
        try {
            this.init("test-send-params");
            message = session.createObjectMessage(obj);
            producer.send(message, deliveryMode, priority, timeToLive);
            System.out.println("sendMessageWithParameters method run");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        this.init(null);
    }

    private void init(String destinationName) {
        this.init("admin", "admin", "tcp://192.168.21.21:61616", destinationName);
    }

    private void init(String username, String password, String brokerURL, String destinationName) {
        try {
            factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection(username, password);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (StringUtils.isNotEmpty(destinationName)) {
                destination = session.createQueue(destinationName);
                producer = session.createProducer(destination);
            } else {
                producer = session.createProducer(null);
            }
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        this.release();
    }

    private void release() {
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


    public static void main(String[] args) {
        ProducerSend4 producer = new ProducerSend4();
        producer.sendMessageWithParameters("3", DeliveryMode.PERSISTENT, 3, 0);
        producer.sendMessageWithParameters("5", DeliveryMode.PERSISTENT, 5, 0);
        producer.sendMessageWithParameters("7", DeliveryMode.PERSISTENT, 7, 0);
        producer.sendMessageWithParameters("9", DeliveryMode.PERSISTENT, 9, 0);
        producer.destory();
    }


}
