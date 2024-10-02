所有的中间件技术都是基于tcp/ip协议基础之上的，rabbitmq遵循的amqp

#### exchange、queue、routing key
- 每个exchange可以bind多个queue
- 每个queue可以和多个exchange进行bind
- direct、topic模式会用到routing key，同一对exchange和queue之间可以设置多个routing key
- exchange、queue可以在生产者或消费者声明


#### work模式
分发策略（默认是轮询机制）
- 轮询模式：一个消费者一条，按均分配；
- 公平模式：根据消费者的消费能力进行公平分发，处理快的处理的多，处理慢的处理的少；按劳分配；


#### ack && nack
一般采用应答模式，即ack=false


#### RabbitMQ的应用场景
##### 异步消息队列优势
1. 解耦、削峰、异步 
- MQ有持久化功能，消息不会丢失
- 死信队列和消息转移等，保证消息的可靠性
- HA镜像模型高可用
2. 高内聚、低耦合


- 消息队列创建投入使用后，要避免删除，修改队列的参数后不会覆盖原有的队列，只会报错


#### 待处理
1. 异步代码demo
```java
//public class Demo {
//    public void makeOrder() {
//        // 1 :保存订单 
//        orderService.saveOrder();
//        // 相关发送
//        relationMessage();
//    }
//
//    public void relationMessage() {
//        // 异步
//        theadpool.submit(new Callable<Object> {
//            public Object call () {
//                // 2： 发送短信服务  
//                messageService.sendSMS("order");
//            }
//        });
//        // 异步
//        theadpool.submit(new Callable<Object> {
//            public Object call () {
//                // 3： 发送email服务
//                emailService.sendEmail("order");
//            }
//        });
//        // 异步
//        theadpool.submit(new Callable<Object> {
//            public Object call () {
//                // 4： 发送短信服务
//                appService.sendApp("order");
//            }
//        });
//        // 异步
//        theadpool.submit(new Callable<Object> {
//            public Object call () {
//                // 4： 发送短信服务
//                appService.sendApp("order");
//            }
//        });
//    }
//}
```

2. 分布式事务的可靠消费和可靠生产
3. 索引、缓存、静态化处理的数据同步
4. 流量监控
5. 日志监控（ELK）
6. autodelete