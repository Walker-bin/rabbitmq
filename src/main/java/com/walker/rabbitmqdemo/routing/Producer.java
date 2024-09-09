package com.walker.rabbitmqdemo.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
* rabbitmq的routing发布模式
*   - fanout 发布订阅模式：交换机会将消息发送给所有bind该交换机的队列
*   - direct 在fanout的基础上添加了routing key，exchange绑定（bind）每个queue时都会设置一个routing key，发布消息时也会设置一个routing key，
*  消息只会发布给已bind的且routing key相同的queue。
*   - topic 与direct模式主要的差别在与routing key的匹配策略，引入* #等模糊匹配字符：*表示有且只有一个；#表示大于等于0个匹配
* 如com.msg.order.app可以匹配到com.#   #.com.#  等  但是不会匹配到*.order.*
 */
public class Producer {

    //此处exchange与queue是在web界面绑定的，代码部分没有表现
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
            //4、通过创建交换机，声明队列、绑定关系、路由key、发送消息、接收消息----创建队列的话不再需要
            /*
             *  如果该队列名不存在，则会创建。Rabbitmq不允许创建两个相同的队列名称，否则会报错。
             *  @params1： queue 队列的名称
             *  @params2： durable 队列是否持久化，mq服务器重启后队列是否还存在。非持久化队列也会存盘，但是会随服务器重启丢失
             *  @params3： exclusive 是否排他，即是否私有的，如果为true,会对当前队列加锁，其他的通道不能访问，并且连接自动关闭
             *  @params4： autoDelete 是否自动删除，当最后一个消费者断开连接之后是否自动删除消息。
             *  @params5： arguments 可以设置队列附加参数，设置队列的有效期，消息的最大长度，队列的消息生命周期等等。
             * */
            //String queue = "queue1"
            //channel.queueDeclare(queue, false, false, false, null);
            //5、准备消息内容
            String message = "Hello World!";
            //6、准备交换机 fanout、direct、topic
            //String exchange = "fanout_exchange";
            //String exchange = "direct_exchange";
            String exchange = "topic_exchange";
            //7、定义路由key fanout、direct、topic
            //String routing_key = "";
            //String routing_key = "msg";
            String routing_key = "com.msg.order";
            //8、指定交换机类型 fanout、direct、topic
            //String type = "fanout";
            //String type = "direct";
            String type = "topic";
            //6、发送消息给队列queue
            /**
             *  @params1： exchange 交换机
             *  @params1： routingKey 队列/路由key
             *  @params1： props 消息的状态控制
             *  @params1： byte[] body 消息主体
             */
            channel.basicPublish(exchange, routing_key, null, message.getBytes());


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


//
//
    }
}
