package com.walker.rabbitmqdemo.all;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Producer {
    public static void main(String[] args) {
//        1、创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
//        factory.setVirtualHost("/");  //虚拟访问节点
        Connection connection = null;
        Channel channel = null;
        try{
            //2、创建连接connection
            connection = factory.newConnection("生产者");
            //3、通过连接获取通道channel
            channel = connection.createChannel();
            //5、声明交换机及类型 fanout、direct、topic、header
            String exchange = "direct_code_exchange";
            String exchangeType = "direct";
            channel.exchangeDeclare(exchange, exchangeType, true);

            //6、声明队列
            channel.queueDeclare("queue4", true, false, false, null);
            channel.queueDeclare("queue5", true, false, false, null);
            channel.queueDeclare("queue6", true, false, false, null);
            //7、绑定exchange与queue
            channel.queueBind("queue4", exchange, "order");
            channel.queueBind("queue5", exchange, "order");
            channel.queueBind("queue6", exchange, "msg");
            //8、准备消息内容
            String message = "Hello World!";
            //9、发送消息给队列queue
            channel.basicPublish(exchange, "order", null, message.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //10、关闭连接
            if (channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
            // 11、关闭通道
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
