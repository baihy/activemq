package com.baihy.text;

/**
 * @projectName: activemq
 * @packageName: com.baihy.text
 * @description:
 * @author: huayang.bai
 * @date: 2019/01/16 8:30
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.consumer();
    }

    private void producer() {
        TextProducer producer = new TextProducer();
        producer.sendMessage("hello ActiveMQ");
    }

    private void consumer() {
        TextConsumer consumer = new TextConsumer();
        String result = consumer.consumerTextMessage();
        System.out.println("消息的内容是：" + result);
    }

}
