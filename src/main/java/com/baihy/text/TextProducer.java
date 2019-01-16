package com.baihy.text;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @projectName: activemq
 * @packageName: com.baihy.text
 * @description: 发送一个字符串文本消息到ActiveMQ中。
 * @author: huayang.bai
 * @date: 2019/01/15 16:58
 */
public class TextProducer {

    /**
     * 发送消息到ActiveMQ中，具体的消息内容为参数信息
     * 开发JSM的过程使用的是：java提供的JSM接口。所有的接口都在javax.jms包下
     *
     * @param data 消息的内容
     */
    public void sendMessage(String data) {
        //JMS规范的链接工厂接口
        ConnectionFactory factory = null;
        //JMS规范的连接接口
        Connection connection = null;
        //JMS规范的目的地接口
        Destination destination = null;
        // JMS规范的会话接口
        Session session = null;
        // JMS规范中的消息生产者接口
        MessageProducer producer = null;
        // JMS规范中的消息对象
        Message message = null;
        try {
            /**
             * 创建连接ActiveMQ服务的链接工厂对象
             * 参数分别是：用户名，密码，和链接地址
             * 没有参数时是没有用户验证。
             * 有参数时，是有用户验证。
             */
            factory = new ActiveMQConnectionFactory("tcp://192.168.21.21:61616");
            /**
             * 通过链接工厂创建链接
             * 创建链接的方法有重载：其中createConnection(String userName, String password)
             * 可以在创建链接工厂时，只传递连接地址，不传用户认证信息，可以在创建链接的时候，传入认证信息。
             */
            connection = factory.createConnection("admin", "admin");
            /**
             * connection = factory.createConnection("username", "password");
             */
            connection.start();
            /**
             * 建议启动连接，消息的发送不是必须启动连接，消息的消费者是必须启动连接。
             * 原因是：在producer在发送消息的时候，会检查是否启动了链接，如果未启动，自动启动。
             *  如果有特殊的配置。建议配置完成后，再启动。
             */
            
            /**
             * 通过链接对象，创建会话对象，必须绑定目的地。
             *  创建会话对象时，必须传两个参数：1.是否支持事务，如何确认消息处理
             *  transacted：是否支持事务，数据类型是boolean，true表示支持，false表示不支持。
             *     true:支持事务，第二个参数对producer来说默认无效，建议传递的数据是:Session.SESSION_TRANSACTED
             *     false:不支持事务，常用参数，第二个参数必须传递，且必须有效
             *     当我们的消息是批量发送的时候，建议开启事务，如果消息是实时发送的话，不建议开启事务。
             *  acknowledgeMode：消息确认模式。如何确认消息的处理，使用确认机制实现的。
             *         AUTO_ACKNOWLEDGE：自动确认消息。消息的消费者处理消息后，自动确认。（常用），商业开发不推荐。
             *         CLIENT_ACKNOWLEDGE：客户端手动确认，消息的消费者处理后，必须手工确认。如果消费者不手工确认的话，就相当于该条消息没有被消费。
             *         DUPS_OK_ACKNOWLEDGE：有副本的客户端手动确认（就是一个消息可以被多次处理）
             *          一个消息可以多次处理。
             *          可以降低session的消耗，在可以容忍的重复消息时使用（不推荐使用）
             */
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            /**
             * 通过session来创建目的地，参数是目的地的名称，是目的地的唯一标记。
             */
            destination = session.createQueue("first-queue");
            /**
             * 通过session，依据目的地来创建producer。注意：创建producer的时候，也可以不指定目的地(session.createProducer(null);)。
             * 这个时候，需要在发送消息的时候，指定目的地。producer.send(destination, message);
             */
            producer = session.createProducer(destination);
            message = session.createTextMessage(data);
            // 发送消息的方法，如果发送消息失败，则会抛出异常。
            producer.send(message);
            /**
             * 如果我们的消息发送的太多了，堆积到了activemq中，会造成内存的压力问题。可能会导致activemq的崩溃，如果出现这种问题，就是代码的问题。可能是没有限流或customer太少了
             */
            System.out.println("消息已发出!!!");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            /**
             * 回收资源
             */
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

}
