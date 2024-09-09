package com.walker.rabbitmqdemo.all;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Consumer {

    private static Runnable runnable = () -> {
        //1、创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        //2、设置连接属性
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        //factory.setVirtualHost("/");  //虚拟访问节点

        //获取队列名称
        final String queue = Thread.currentThread().getName();

        Connection connection = null;
        Channel channel = null;
        try{
            //3、创建连接connection
            connection = factory.newConnection("生产者");
            //4、通过连接获取通道channel
            channel = connection.createChannel();
            //5、申明队列储存消息，如果队列已创建，此处不需要再创建
            //channel.queueDeclare(queue, false, false, false, null);
            channel.basicConsume(queue, true, new DeliverCallback() {
                //一个rabbitmq消息消息传递时通知的回调接口
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    //message.getBody()返回的是byte[]，将其转化为string
                    System.out.println("收到消息" + new String(message.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                //消费者取消时通知的回调接口
                @Override
                public void handle(String consumerTag) throws IOException {
                    System.out.println("接受失败");
                }
            });
            System.out.println("开始接收消息");
            System.in.read();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //7、关闭连接
            if (channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
            // 8、关闭通道
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public static void main(String[] args) {
        new Thread(runnable, "queue4").start();
        new Thread(runnable, "queue5").start();
        new Thread(runnable, "queue6").start();
    }
}
