# RabbitMQ

## RabbitMQ简介

以熟悉的电商场景为例，如果商品服务和订单服务是两个不同的微服务，在下单的过程中订单服务需要调用商品服务进行扣库存操作。按照传统的方式，下单过程要等到调用完毕之后才能返回下单成功，如果网络产生波动等原因使得商品服务扣库存延迟或者失败，会带来较差的用户体验，如果在高并发的场景下，这样的处理显然是不合适的，那怎么进行优化呢？这就需要消息队列登场了。

消息队列提供一个异步通信机制，消息的发送者不必一直等待到消息被成功处理才返回，而是立即返回。消息中间件负责处理网络通信，如果网络连接不可用，消息被暂存于队列当中，当网络畅通的时候在将消息转发给相应的应用程序或者服务，当然前提是这些服务订阅了该队列。如果在商品服务和订单服务之间使用消息中间件，既可以提高并发量，又降低服务之间的耦合度。

RabbitMQ就是这样一款我们苦苦追寻的消息队列。RabbitMQ是一个开源的消息代理的队列服务器，用来通过普通协议在完全不同的应用之间共享数据。

RabbitMQ是使用Erlang语言来编写的，并且RabbitMQ是基于AMQP协议的。Erlang语言在数据交互方面性能优秀，有着和原生Socket一样的延迟，这也是RabbitMQ高性能的原因所在。可谓“人如其名”，RabbitMQ像兔子一样迅速。

![](img/rabbitmq.jpg)



RabbitMQ除了像兔子一样跑的很快以外，还有这些特点：

- 开源、性能优秀，稳定性保障
- 提供可靠性消息投递模式、返回模式
- 与Spring AMQP完美整合，API丰富
- 集群模式丰富，表达式配置，HA模式，镜像队列模型
- 保证数据不丢失的前提做到高可靠性、可用性

MQ典型应用场景：

- 异步处理。把消息放入消息中间件中，等到需要的时候再去处理。
- 流量削峰。例如秒杀活动，在短时间内访问量急剧增加，使用消息队列，当消息队列满了就拒绝响应(这里应该做限流)，跳转到错误页面，这样就可以使得系统不会因为超负载而崩溃。
- 日志处理
- 应用解耦。假设某个服务A需要给许多个服务（B、C、D）发送消息，当某个服务（例如B）不需要发送消息了，服务A需要改代码再次部署；当新加入一个服务（服务E）需要服务A的消息的时候，也需要改代码重新部署；另外服务A也要考虑其他服务挂掉，没有收到消息怎么办？要不要重新发送呢？是不是很麻烦，使用MQ发布订阅模式，服务A只生产消息发送到MQ，B、C、D从MQ中读取消息，需要A的消息就订阅，不需要了就取消订阅，服务A不再操心其他的事情，使用这种方式可以降低服务或者系统之间的耦合。

## AMQP协议和RabbitMQ

先了解一下AMQP协议中间的几个重要概念：

- Server：接收客户端的连接，实现AMQP实体服务。
- Connection：连接，应用程序与Server的网络连接，TCP连接。
- Channel：信道，消息读写等操作在信道中进行。客户端可以建立多个信道，每个信道代表一个会话任务。
- Message：消息，应用程序和服务器之间传送的数据，消息可以非常简单，也可以很复杂。有Properties和Body组成。Properties为外包装，可以对消息进行修饰，比如消息的优先级、延迟等高级特性；Body就是消息体内容。
- Virtual Host：虚拟主机，用于逻辑隔离。一个虚拟主机里面可以有若干个Exchange和Queue，同一个虚拟主机里面不能有相同名称的Exchange或Queue。
- Exchange：交换器，接收消息，按照路由规则将消息路由到一个或者多个队列。如果路由不到，或者返回给生产者，或者直接丢弃。RabbitMQ常用的交换器常用类型有direct、topic、fanout、headers四种，后面详细介绍。
- Binding：绑定，交换器和消息队列之间的虚拟连接，绑定中可以包含一个或者多个RoutingKey。
- RoutingKey：路由键，生产者将消息发送给交换器的时候，会发送一个RoutingKey，用来指定路由规则，这样交换器就知道把消息发送到哪个队列。路由键通常为一个“.”分割的字符串，例如“com.rabbitmq”。
- Queue：消息队列，用来保存消息，供消费者消费。

> 我们完全可以直接使用 Connection 就能完成信道的工作，为什么还要引入信道呢?

> 试想这样一个场景， 一个应用程序中有很多个线程需要从 RabbitMQ 中消费消息，或者生产消息，那么必然需要建立很多个 Connection，也就是许多个 TCP 连接。然而对于操作系统而言，建立和销毁 TCP 连接是非常昂贵的开销，如果遇到使用高峰，性能瓶颈也随之显现。 RabbitMQ 采用 TCP 连接复用的方式，不仅可以减少性能开销，同时也便于管理 。

下图是AMQP的协议模型：

![](img/amqp.png)

正如图中所看到的，AMQP协议模型有三部分组成：生产者、消费者和服务端。

生产者是投递消息的一方，首先连接到Server，建立一个连接，开启一个信道；然后生产者声明交换器和队列，设置相关属性，并通过路由键将交换器和队列进行绑定。同理，消费者也需要进行建立连接，开启信道等操作，便于接收消息。

接着生产者就可以发送消息，发送到服务端中的虚拟主机，虚拟主机中的交换器根据路由键选择路由规则，然后发送到不同的消息队列中，这样订阅了消息队列的消费者就可以获取到消息，进行消费。

最后还要关闭信道和连接。

RabbitMQ是基于AMQP协议实现的，其结构如下图所示，和AMQP协议简直就是一模一样。

![](img/rabbitamqp.png)

## 常用交换器

RabbitMQ常用的交换器类型有direct、topic、fanout、headers四种。

### Direct Exchange

该类型的交换器将所有发送到该交换器的消息被转发到RoutingKey指定的队列中，也就是说路由到BindingKey和RoutingKey完全匹配的队列中。

![](img/rabbitdirect.png)

### Topic Exchange

该类型的交换器将所有发送到Topic Exchange的消息被转发到所有RoutingKey中指定的Topic的队列上面。

Exchange将RoutingKey和某Topic进行模糊匹配，其中“*”用来匹配一个词，“#”用于匹配一个或者多个词。例如“com.#”能匹配到“com.rabbitmq.oa”和“com.rabbitmq”；而"login.*"只能匹配到“com.rabbitmq”。

![](img/rabbittopic.png)

### Fanout Exchange

该类型不处理路由键，会把所有发送到交换器的消息路由到所有绑定的队列中。优点是转发消息最快，性能最好。

![](img/rabbitfanout.png)

### Headers Exchange

该类型的交换器不依赖路由规则来路由消息，而是根据消息内容中的headers属性进行匹配。headers类型交换器性能差，在实际中并不常用。

## 安装和使用入门

本文使用Docker进行安装RabbitMQ,快速体验RabbitMQ的魅力。

- 进入[官方下载地址](https://www.rabbitmq.com/download.html)，选择使用Docker安装，可以跳转到[dockerhub](https://hub.docker.com/_/rabbitmq/)查看镜像。
- 我选择3-management进行安装，带有management是含有管理界面的。
- 拉取镜像和启动：docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
- 查看镜像：

```
[root@localhost ~]# docker images
REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
docker.io/rabbitmq   3-management        800294d91e31        5 weeks ago         198 MB

```

- 我的安装在了自己的虚拟机上，ip地址是：192.168.111.20,打开浏览器访问http://192.168.111.20:15672
- ![img](img\rabbitdocker1.png)
- 进行填写账号密码：默认账号密码都是guest.
- ![img](img\rabbitdocker2.png)

到此，RabbitMQ已经安装并运行起来了。

## Springboot集成RabbitMQ

通过在Spring Boot应用中整合RabbitMQ，并实现一个简单的发送、接收消息的例子来对RabbitMQ有一个直观的感受和理解。

在Spring Boot中整合RabbitMQ是一件非常容易的事，因为之前我们已经介绍过Starter POMs，其中的AMQP模块就可以很好的支持RabbitMQ，下面我们就来详细说说整合过程：

### 1、POM依赖

在pom.xml中引入如下依赖内容，其中spring-boot-starter-amqp用于支持RabbitMQ。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.zao</groupId>
    <artifactId>boot-rabbitmq</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>boot-rabbitmq</name>
    <description>Demo rabbitmq project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

### 2、配置文件

在application.properties中配置关于RabbitMQ的连接和用户信息，用户可以回到上面的安装内容，在管理页面中创建用户。

```properties
spring.rabbitmq.host=192.168.111.20
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
#必须指定一个vhost
spring.rabbitmq.virtual-host=/
```

**注意：vhost的配置，RabbitMq会有一个默认的vhost：`/`,不配置vhost,是收不到消息的。**

### 3、Direct Exchanger

多个listener会负载均衡的方式消费消息，每个消息只被消费一次。

#### 配置

```java
@Configuration
public class DirectConfig {
    /**
     * 创建一个队列 hello
     * @return
     */
    @Bean
    public Queue helloQueue(){
        return new Queue("hello");
    }
}

```

#### 发送和接收消息

```java
@RestController
public class DirectController {

    @Resource
    private AmqpTemplate amqpTemplate;

    @GetMapping("/direct/send")
    public String send(String msg){
        System.out.println("send :" + msg);
        amqpTemplate.convertAndSend("hello",msg);
        return "ok";
    }

    @RabbitListener(queues = "hello")
    public void processA(String msg) {
        System.out.println("Receiver A:" + msg);
    }

    @RabbitListener(queues = "hello")
    public void processB(String msg) {
        System.out.println("Receiver B:" + msg);
    }

}

```

### 4、Topic Exchange

topic 是RabbitMQ中最灵活的一种方式，可以根据routing_key自由的绑定不同的队列。 首先对topic规则配置，这里使用两个队列来测试

#### 配置

```
@Configuration
public class TopicConfig {

    private static  final  String message = "topic.message";
    private static  final  String messages = "topic.messages";

    @Bean
    public Queue queueMessage(){
        return new Queue(message);
    }

    @Bean
    public Queue queueMessages(){
        return new Queue(messages);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("myTopicExchange");
    }

    @Bean
    public Binding bindingExchangeMessage(Queue queueMessage,TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessage).to(topicExchange).with(message);
    }

    @Bean
    public Binding bindingExchangeMessages(Queue queueMessages,TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessages).to(topicExchange).with(messages);
    }
}

```



#### 发送和接收消息

```
@RestController
public class TopicController {

    @Resource
    private AmqpTemplate amqpTemplate;

    @GetMapping("/topic/send")
    public String send(String msg){
        amqpTemplate.convertAndSend("myTopicExchange","topic.1","MSG All " + msg);
        amqpTemplate.convertAndSend("myTopicExchange","topic.message","MSG 1 " + msg);
        amqpTemplate.convertAndSend("myTopicExchange","topic.messages","MSG 2 " + msg);
        return "ok";
    }

    @RabbitListener(queues = "topic.message")
    public void process1(String msg){
        System.out.println("topic.message "+ msg);
    }

    @RabbitListener(queues = "topic.messages")
    public void process2(String msg){
        System.out.println("topic.messages "+ msg);
    }

}

```



### 5、Fanout Exchange

Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。

#### 配置

```
@Configuration
public class FanoutConfig {

    @Bean
    public Queue aMessage() {
        return new Queue("fanout.a");
    }

    @Bean
    public Queue bMessage() {
        return new Queue("fanout.b");
    }

    @Bean
    public Queue cMessage() {
        return new Queue("fanout.c");
    }

    @Bean
    public FanoutExchange myFanoutExchanger() {
        return new FanoutExchange("myFanoutExchanger");
    }

    @Bean
    public Binding bindingExchangeA(Queue aMessage, FanoutExchange myFanoutExchanger) {
        return BindingBuilder.bind(aMessage).to(myFanoutExchanger);
    }

    @Bean
    public Binding bindingExchangeB(Queue bMessage, FanoutExchange myFanoutExchanger) {
        return BindingBuilder.bind(bMessage).to(myFanoutExchanger);
    }

    @Bean
    public Binding bindingExchangeC(Queue cMessage, FanoutExchange myFanoutExchanger) {
        return BindingBuilder.bind(cMessage).to(myFanoutExchanger);
    }
}
```



#### 发送和接收消息



```
@RestController
public class FanoutController {

    @Resource
    private AmqpTemplate amqpTemplate;

    @GetMapping("/fanout/send")
    public String send(String msg) {
        amqpTemplate.convertAndSend("myFanoutExchanger", "", msg);
        return "ok";
    }

    @RabbitListener(queues = "fanout.a")
    public void processA(String message) {
        System.out.println("processA " + message);
    }

    @RabbitListener(queues = "fanout.b")
    public void processB(String message) {
        System.out.println("processB " + message);
    }

    @RabbitListener(queues = "fanout.c")
    public void processC(String message) {
        System.out.println("processC " + message);
    }

}

```



结果说明，绑定到fanout交换机上面的队列都收到了消息

# 参考资料

https://www.cnblogs.com/sgh1023/p/11217017.html

https://www.jianshu.com/p/c85ac0063dbf