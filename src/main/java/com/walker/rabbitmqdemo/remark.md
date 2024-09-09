所有的中间件技术都是基于tcp/ip协议基础之上的，rabbitmq遵循的amqp

#### exchange、queue、routing key
- 每个exchange可以bind多个queue
- 每个queue可以和多个exchange进行bind
- direct、topic模式会用到routing key，同一对exchange和queue之间可以设置多个routing key
- exchange、queue可以在生产者或消费者声明


