---
typora-copy-images-to: img

---

# RabbitMQ快速入门

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

>  Spring-boot集成RabbitMq https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-amqp

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



### 6、RPC？

### 7、延时队列？

### 8、事务？



## 参考资料

https://www.cnblogs.com/sgh1023/p/11217017.html

https://www.jianshu.com/p/c85ac0063dbf

# RabbitMq核心概念和原理

## AMQP协议

AMQP，即Advanced Message Queuing Protocol，高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。消息中间件主要用于组件之间的解耦，消息的发送者无需知道消息使用者的存在，反之亦然。

AMQP的主要特征是面向消息、队列、路由（包括点对点和发布/订阅）、可靠性、安全。RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写，支持多种客户端，如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX。用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。

## RabbitMq核心概念

### Broker

简单来说就是消息队列服务器实体。

### producer

消息生产者，就是投递消息的程序。

### consumer

消息消费者，就是接受消息的程序。

### vhost

虚拟主机，一个broker里可以开设多个vhost，用作权限分离，把不同的系统使用的rabbitmq区分开，共用一个消息队列服务器，但看上去就像各自在用不用的rabbitmq服务器一样。

### Connection

一个网络连接，比如TCP/IP套接字连接。

### channel

消息通道，是建立在真实的TCP连接内的虚拟连接（是我们与RabbitMQ打交道的最重要的一个接口）。仅仅创建了客户端到Broker之间的连接后，客户端还是不能发送消息的,需要为每一个Connection创建Channel，AMQP协议规定只有通过Channel才能执行AMQP的命令。AMQP的命令都是通过信道发送出去的（我们大部分的业务操作是在Channel这个接口中完成的，包括定义Queue、定义Exchange、绑定Queue与Exchange、发布消息等。）。每条信道都会被指派一个唯一ID。在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务,理论上无限制，减少TCP创建和销毁的开销，实现共用TCP的效果。之所以需要Channel，是因为TCP连接的建立和释放都是十分昂贵的，如果一个客户端每一个线程都需要与Broker交互，如果每一个线程都建立一个TCP连接，暂且不考虑TCP连接是否浪费，就算操作系统也无法承受每秒建立如此多的TCP连接。

**注1：一个生产者或一个消费者与MQ服务器之间只有一条TCP连接** 

**注2：RabbitMQ建议客户端线程之间不要共用Channel，至少要保证共用Channel的线程发送消息必须是串行的，但是建议尽量共用Connection。**

### Exchange

消息交换机，生产者不是直接将消息投递到Queue中的，实际上是生产者将消息发送到Exchange（交换器，下图中的X），由Exchange将消息路由到一个或多个Queue中（或者丢弃）。

![640?wx_fmt=jpeg](img\exchange.jpg)



#### Exchange Types 

RabbitMQ常用的Exchange Type有fanout、direct、topic、headers这四种（AMQP规范里还提到两种Exchange Type，分别为system与自定义，这里不予以描述），之后会分别进行介绍。

### Queue

消息队列载体，每个消息都会被投入到一个或多个队列。

Queue（队列）是RabbitMQ的内部对象，用于存储消息，用下图表示。

![640?wx_fmt=jpeg](img\queue.jpg)

RabbitMQ中的消息都只能存储在Queue中，生产者（下图中的P）生产消息并最终投递到Queue中，消费者（下图中的C）可以从Queue中获取消息并消费。

![640?wx_fmt=jpeg](img\queue2.jpg)

多个消费者可以订阅同一个Queue，这时Queue中的消息会被平均分摊给多个消费者进行处理，而不是每个消费者都收到所有的消息并处理。

![640?wx_fmt=jpeg](img\queue3.jpg)

### Binding

绑定，它的作用就是把exchange和queue按照路由规则绑定起来，这样RabbitMQ就知道如何正确地将消息路由到指定的Queue了。

![640?wx_fmt=jpeg](img\binding.jpg)



### Routing Key

路由关键字，生产者在将消息发送给Exchange的时候，一般会指定一个routing key，来指定这个消息的路由规则，而这个routing key需要与Exchange Type及binding key联合使用才能最终生效。

![640?wx_fmt=jpeg](img\routingkey.jpg)

在Exchange Type与binding key固定的情况下（在正常使用时一般这些内容都是固定配置好的），我们的生产者就可以在发送消息给Exchange时，通过指定routing key来决定消息流向哪里。

### **Prefetch count**

 前面我们讲到如果有多个消费者同时订阅同一个Queue中的消息，Queue中的消息会被平摊给多个消费者。这时如果每个消息的处理时间不同，就有可能会导致某些消费者一直在忙，而另外一些消费者很快就处理完手头工作并一直空闲的情况。我们可以通过设置prefetchCount来限制Queue每次发送给每个消费者的消息数，比如我们设置prefetchCount=1，则Queue每次给每个消费者发送一条消息；消费者处理完这条消息后Queue会再给该消费者发送一条消息。

![640?wx_fmt=jpeg](img\prefetchcount.jpg)

![640?wx_fmt=jpeg](img\prefetchcount1.jpg)

## 消息队列的使用过程

在AMQP模型中，Exchange是接受生产者消息并将消息路由到消息队列的关键组件。ExchangeType和Binding决定了消息的路由规则。所以生产者想要发送消息，首先必须要声明一个Exchange和该Exchange对应的Binding。

在Rabbit MQ中，声明一个Exchange需要三个参数：ExchangeName，ExchangeType和Durable。ExchangeName是该Exchange的名字，该属性在创建Binding和生产者通过publish推送消息时需要指定。ExchangeType，指Exchange的类型，在RabbitMQ中，有三种类型的Exchange：direct ，fanout和topic，不同的Exchange会表现出不同路由行为。Durable是该Exchange的持久化属性，这个会在消息持久化章节讨论。

### 建立一个Exchange

![640?wx_fmt=jpeg](img\exchange1.jpg)

### 声明一个Binding

声明一个Binding需要提供一个QueueName，ExchangeName和BindingKey。

![640?wx_fmt=jpeg](img\binding1.jpg)

### 消息发送的过程

![640?wx_fmt=jpeg](img\sendmsg.jpg)

1. 建立连接Connection。由producer和consumer创建连接，连接到broker的物理节点上。
2. 建立消息Channel。Channel是建立在Connection之上的，一个Connection可以建立多个Channel。producer连接Virtual Host 建立Channel，Consumer连接到相应的queue上建立Channel。
3. 发送消息。由Producer发送消息到Broker中的Exchange中。
4. 路由转发。生产者Producer在发送消息时，都需要指定一个RoutingKey和Exchange，Exchange收到消息后可以看到消息中指定的RoutingKey，再根据当前Exchange的ExchangeType,按一定的规则将消息转发到相应的queue中去。
5. 消息接收。Consumer会监听相应的queue，一旦queue中有可以消费的消息，queue就将消息发送给Consumer端。
6. 消息确认。当Consumer完成某一条消息的处理之后，需要发送一条ACK消息给对应的Queue。Queue收到ACK信息后，才会认为消息处理成功，并将消息从Queue中移除；如果在对应的Channel断开后，Queue没有收到这条消息的ACK信息，该消息将被发送给另外的Channel。至此一个消息的发送接收流程走完了。消息的确认机制提高了通信的可靠性。

## exchange 与 Queue 的路由机制

exchange 将消息发送到哪一个queue是由exchange type 和bing 规则决定的，目前常用的有3种exchange，Direct exchange, Fanout exchange, Topic exchange 。Direct exchange 直接转发路由，其实现原理是通过消息中的routkey，与queue 中的routkey 进行比对，若二者匹配，则将消息发送到这个消息队列。通常使用这个。

### Direct exchange 路由

![640?wx_fmt=jpeg](img\directex.jpg)

以上图的配置为例，我们以routingKey=”error”发送消息到Exchange，则消息会路由到Queue1（amqp.gen-S9b…，这是由RabbitMQ自动生成的Queue名称）和Queue2（amqp.gen-Agl…）；如果我们以routingKey=”info”或routingKey=”warning”来发送消息，则消息只会路由到Queue2。如果我们以其他routingKey发送消息，则消息不会路由到这两个Queue中。

### Fanout exchange 复制分发路由

该路由不需要routkey，当exchange收到消息后，将消息复制多份转发给与自己绑定的消息队列。

![640?wx_fmt=jpeg](img\fanoutex.jpg)

上图中，生产者（P）发送到Exchange（X）的所有消息都会路由到图中的两个Queue，并最终被两个消费者（C1与C2）消费。

### topic exchange 通配路由

topic exchange 通配路由，是direct exchange的通配符模式，消息中的routkey可以写成通配的模式，exchange支持“#”和“*” 的通配。收到消息后，将消息转发给所有符合匹配表达式的queue。

![640?wx_fmt=jpeg](img\topicex.jpg)

以上图中的配置为例，routingKey=”quick.orange.rabbit”的消息会同时路由到Q1与Q2，routingKey=”lazy.orange.fox”的消息会路由到Q1，routingKey=”lazy.brown.fox”的消息会路由到Q2，routingKey=”lazy.pink.rabbit”的消息会路由到Q2（只会投递给Q2一次，虽然这个routingKey与Q2的两个bindingKey都匹配）；routingKey=”quick.brown.fox”、routingKey=”orange”、routingKey=”quick.orange.male.rabbit”的消息将会被丢弃，因为它们没有匹配任何bindingKey。

**需要注意的一点只有queue具有 保持消息的功能，exchange不能保存消息。**

### headers 路由

headers类型的Exchange不依赖于routing key与binding key的匹配规则来路由消息，而是根据发送的消息内容中的headers属性进行匹配。在绑定Queue与Exchange时指定一组键值对；当消息发送到Exchange时，RabbitMQ会取到该消息的headers（也是一个键值对的形式），对比其中的键值对是否完全匹配Queue与Exchange绑定时指定的键值对；如果完全匹配则消息会路由到该Queue，否则不会路由到该Queue。

## durability 持久化与非持久化队列

![640?wx_fmt=jpeg](img\durability.jpg)

如上图，在Features字段里有一个D,就是持久化队列，英文durable（持久的）。

持久化队列会被保存在磁盘中，固定并持久的存储，当Rabbit服务重启后，该队列会保持原来的状态在RabbitMQ中被管理，而非持久化队列不会被保存在磁盘中，Rabbit服务重启后队列就会消失。

如果需要队列的完整性，数据在队列中的保存是必须不允许丢失的，那么可以使用持久化。而当需要获取的信息是实时的，或者是随机的信息，不需要信息的精确性或完整性，但是追求获取性能，可以选择非持久化队列。



## 参考资料

https://blog.csdn.net/mingongge/article/details/99512557?ops_request_misc=&request_id=&biz_id=102&utm_term=rabbit%20mq%20%E9%9B%86%E7%BE%A4&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-0-99512557.pc_search_result_hbase_insert



# RabbitMq分布式集群架构和高可用性



## 设计集群的目的

- 允许消费者和生产者在RabbitMQ节点崩溃的情况下继续运行

- 通过增加更多的节点来扩展消息通信的吞吐量

  

## Rabbit MQ的分布式部署方式

Rabbit MQ的分布式部署方式总共有三种，分别是**集群部署**、**Federation**(联邦)部署 和 **Shovel部署**。有趣的是，这三种部署方式并不是互斥的，而是可以联合使用的。用户可以根据实际情况，选择其中的一种或多种部署方式来满足自己的实际应用需求。这些部署方式的联合使用固然提高应用程序的性能和灵活性等，但同时也提高了部署的复杂性。

### cluster(集群部署):

1. 不支持跨网段，用于同一个网段内的局域网
2. 可以随意的动态增加或者减少
3. 节点之间需要运行相同版本的RabbitMQ和Erlang

### federation(联邦)

应用于广域网，允许单台服务器上的交换机或队列接收发布到另一台服务器上交换机或队列的消息，可以是单独机器或集群。federation队列类似于单向点对点连接，消息会在联盟队列之间转发任意次，直到被消费者接受。通常使用federation来连接internet上的中间服务器，用作订阅分发消息或工作队列。

### shovel

连接方式与federation的连接方式类似，但它工作在更低层次。可以应用于广域网

三种部署方式各有其优缺点，需要根据具体的业务场景确定具体的部署方式，现在就来具体看一下集群部署方式的应用场景和底层实现。

## 集群部署

在了解集群部署之前，先来介绍一下集群的概念吧。

集群就是将多个服务器部署在同一个网络区间内，集群的多个服务器可以看成是一个整体，一个逻辑上的服务器，因此集群可以提高应用程序的吞吐量和可靠性。

吞吐量很好理解，以前是一个机器干一个活，现在多了好几个机器，干的活肯定比以前多。
可靠性也很好理解，以前一个应用是部署在一个机器上，这个机器挂掉了，整个应用程序就不能工作。但是现在是多个机器，你挂掉一个，没事，还有其它机器可以继续工作，至少不会导致应用程序不能工作了。

那么什么时候需要用到集群部署呢，就是当你服务器抗不住你应用的程序的吞吐量时，比如你的应用程序一秒几十万的消息吞吐量，或者是更大，这个时候就需要用到Rabbit MQ的集群部署了。

Rabbit MQ的集群模式共分为以下两种：

- 普通集群部署
- 镜像集群模式

Rabbit MQ的集群配置方式也有三种：

- 通过 abbi mqct 工具配
- 通过rabbitmq config 配置文件配置
- 通过 bitmq-aut cluster 插件配置

### 普通集群模式(默认的集群模式)

普通集群模式就是将多台Rabbit MQ服务器连接组成一个集群，在连接过程中需要正确的Erlang Cookie和节点名称才能保证机器之间相互进行连接访问，并且集群需要要局域网内进行部署。

集群中的每一台服务器可以说成是集群的一个节点，同学们应该知道，每一个Rabbit MQ服务器都是由连接池、信道、交换机、队列等组成，Rabbit MQ服务器的结构组成如下图所示：

![Rabbit MQ的结构](img\ribbitstruct.jpg)

#### **多机集群结构**

多机集群结构图如下图所示：

![在这里插入图片描述](img\rabbit-cluster.jpg)

但是Rabbit MQ的集群不是每个节点都有所有队列的完全拷贝。从上面的图中也可以看出，交换机A的的元数据信息在所有节点上都是一致的，但是存放消息的队列的完整信息都只存在它所创建的节点上，所有其他节点只知道队列的元数据和指向该队列存在的那个节点的指针，元数据信息包括以下内容：



- 队列元数据：队列的名称及属性
- 交换器：交换器的名称及属性
- 绑定关系元数据：交换器与队列或者交换器与交换器之 的绑定关系
- vhost元数据：为 vhost内的队列、交换器和绑定提供命名空间及安全属性。



为什么Rabbit MQ不把所有数据拷贝到所有节点上呢？而是只拷贝元数据信息呢？

- 存储空间：如果每个节点都拥有所有队列的完全拷贝，这样新增节点不但没有新增存储空间，反而增加了更多的冗余数据
- 性能：如果消息的发布需安全拷贝到每一个集群节点，那么新增节点对网络和磁盘负载都会有增加，这样违背了建立集群的初衷，新增节点并没有提升处理消息的能力，最多是保持和单节点相同的性能甚至是更糟。
  

#### **集群节点间的消息流转**

以三个节点(node1、node2、node3)的集群为例来进行说明。消息实体是存在于队列之中的，而节点之间只有相同的元数据信息，假设消息存在于node1节点的A队列上，当消费者从node2节点上的B队列消费时，这时RabbitMQ会临时在node1和node2节点进行消息传输，把A队列上的消息实体传到B队列上，然后发送给消费者。

这个过程其实会对node1节点产生性能瓶颈，因为无论consumer连node1或node2，都会从node1拉取数据。针对这种情况，有一个中庸的做法就是将consumer尽量连接每一个节点。



#### **集群节点类型**

集群节点类型分为以下两类：

- 磁盘节点
- 内存节点

磁盘节点的数据信息是存储在磁盘上的，内存节点的信息是存储在内存上的，因此内存节点的性能要高于磁盘节点。

**注意**： Rabbit MQ要求集群中至少有一个磁盘节点，所有其他节点可以是内存节点，当节点加入和离开集群时，必须通知磁盘节点。

当惟一的磁盘节点奔溃时，

集群可以继续发送或者接收消息，
但是不能执行创建队列、交换器、绑定关系、用户，以及更改权限、添加或删除集群节点的操作了。
也就是说，如果集群中唯一的磁盘节点崩溃，集群仍然可以保持运行，但是直到将该节点恢复到集群前，你无法更改任何东西。所以在建立集群的时候应该保证有两个或者多个磁盘节点的存在。



#### **集群节点异常处理**

当集群节点崩溃时，该节点的队列进程和关联的绑定都会消失。附加在那些队列上的消费者 会丢失其所订阅的信息 井且任何匹配该队列绑定信息的新消息也都会消失。那么面临这种情况应该如何处理呢：

- 持久化处理，当该节点重启的时候可以再次获取到该消息。
- 镜像模式，就是指创建一个镜像节点，镜像节点保存有崩溃节点的所有信息，那么该节点崩溃时，镜像节点可以接替它继续工作，直至崩溃节点重启。



#### 普通集群模式总结

多机集群提高了系统的**吞吐量和可靠性**。但是并没有做到**高可用性**，因为当磁盘节点崩溃的话，其它节点不能进行创建队列、创建交换器等，可以这样说吧，其它内存节点就是为磁盘节点服务的，下面介绍的镜像模式部署解决了这个缺点，实现了集群的**高可用**性。



### 镜像集群模式(RabbitMQ的HA方案)

镜像集群模式其实就是把需要的队列做成镜像队列，然后将镜像队列放在多个节点当中，这种镜像集群模式解决了普通集群模式没有做到的**高可用性**的缺点，镜像集群模式属于Rabbit MQ的高可用性的集群部署方案。

#### 镜像集群模式的结构

镜像集群模式的结构如下图所示：

![在这里插入图片描述](img\rabbit-ha.jpg)



其中master是主节点（存放消息实体的队列），slave是从节点（镜像队列），一个主节点可以有多个从节点，消息实体 经过GM(Guaranteed Multicast)协议在主从镜像节点之间进行广播同步，这样无论哪一台服务器节点宕机了，其它服务器节点照样可以工作，它们的关系如下图所示：
![在这里插入图片描述](img\rabbit-publisher.jpg)



那么当master节点崩溃后，还有slave节点，slave节点会保存消息体，Rabbit MQ规定，当master节点宕机后，“资历最老"的 slave 会被提升为新的 master，根据 slave 加入的时间排序，时间最长的 slave 即为"资历最老”。



#### **镜像队列间的消息流转**

当消费者与master队列建立连接，消费者可以直接从master队列上获取信息，当消费者与slave队列建立连接呢？消费者是从slave队列直接获取数据的吗？当然不是的，消息的流转顺序如下所示：

- slave队列先将消费者的请求转发给master队列

- 然后再由master队列准备好数据返回给slave队列

- 最后由slave队列将消息返回给消费者

那这样就会有一个疑问？消费者的请求都是由master队列进行处理的，那么消息的负载是不是不能够做到有效的均衡呢？



#### **负载均衡**

Rabbit MQ的负载均衡是体现在物理机器层面上的，而不是体现在内存中的队列层面的。这样解释吧，现在有3台物理机，需要创建3个master队列和6个slave队列， 消息的请求负载都在3个master队列上，那么只需要将3个master队列和6个slave队列均匀的分布在3台物理机上，这样在很大程度上实现了每台机器的负载均衡。当然每个master队列消息请求的数量可能会有不同，无法保持绝对的负载均衡。



#### **消息的可靠性**

RabbitMQ的镜像队列使用 publisher confirm 和事务两种机制来保证其消息的可靠性。在事务机制中，只有当前事务在全部镜像中执行之后，客户端才会收到 Tx Commit-Ok 的消息。同样的，在 publisher confirm 机制中，生产者进行当前消息确认的前提是该消息被全部进行所接收了。



#### 镜像集群模式总结

镜像队列的引入可以极大地提升 RabbitMQ 的可用性及可靠性，提供了数据冗余备份、避免单点故障的功能，因此推荐在实际应用中为每个重要的队列都配置镜像。

说了这么多的镜像队列的优点，那么镜像队列就没有缺点了吗？当然不是，那么镜像集群的缺点是什么呢？

首先镜像队列需要为每一个节点都要同步所有的消息实体，所以会导致网络带宽压力很大。
提供了数据的冗余备份，会导致存储压力变大，可能会出现IO瓶颈。



## 参考资料

https://blog.csdn.net/qq_36526036/article/details/105182855?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522161525275016780255246231%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=161525275016780255246231&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_v2~rank_v29_name-13-105182855.pc_search_result_hbase_insert&utm_term=rabbit+mq

# RabbiiMq相关面试题？