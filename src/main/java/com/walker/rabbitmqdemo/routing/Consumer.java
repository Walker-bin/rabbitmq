package com.walker.rabbitmqdemo.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 以多线程模式代替多个消费者进行模拟代码
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
            //接收指定队列的消息

            //5、申明队列储存消息，如果队列已创建，此处不需要再创建
            /*
             *  如果该队列名不存在，则会创建。Rabbitmq不允许创建两个相同的队列名称，否则会报错。
             *  @params1： queue 队列的名称
             *  @params2： durable 队列是否持久化，mq服务器重启后队列是否还存在。非持久化队列也会存盘，但是会随服务器重启丢失
             *  @params3： exclusive 是否排他，即是否私有的，如果为true,会对当前队列加锁，其他的通道不能访问，并且连接自动关闭
             *  @params4： autoDelete 是否自动删除，当最后一个消费者断开连接之后是否自动删除消息。
             *  @params5： arguments 可以设置队列附加参数，设置队列的有效期，消息的最大长度，队列的消息生命周期等等。
             * */
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
        new Thread(runnable, "queue1").start();
        new Thread(runnable, "queue2").start();
        new Thread(runnable, "queue3").start();
    }
}
