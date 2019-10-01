
# Kafka Consumer Groups Load tests

## Release Notes

* **2019-09-28**: First version, all working just fine, but documentation :smile: 
* **2019-10-01**: This documentation

# Study Case

## The Scenario

In a recent case (september/2019) I helped to analise a scenario where an application, responsible to "transform some data", had to be a simple but powerfull ETL (Extract, transform, load), from a message as fixed lenth fields to a metadata JSON (validated).

To test this scenario, I "borrowed" another project I just made (https://github.com/ricardo-comar/kafka-balanced-consumers) to simplify the tests, acting as a "producer" and response time analyser. 

The producer has an endpoint REST, to receive a request, transform into a Message, send to a Inbound queue in ActiveMQ, and keep the request alive until other message came from the Kafka topic, to reply the response.

![](img/architecture.png)

A request controll was done, by using a custom header with a UUID  identifying the request, and replyed also on a custom header inside the response, to release the REST lock and replying it.

## Simulating 

* **Kafka Cluster (main folder)**: The cluster was made by a docker-compose yaml, providing 3 instances together with 3 zookeeper instances.
* **MySQL Instance (main folder)**: Also provided by the docker-compose
* **Load Balancer (border-service)**: A simple Spring Boot instance, set up to provide a load balance with zool, eureka and Hystrix.
* **Producer (kafka-producer)**: The Producer instance, provided by Spring Boot Web with two endpoints. The first receives a message with this payload below, and answers a HTTP 200 payload on success:
```
{ 
    "id": "ABC", //Unique ID for each request
    "payload": "Team ABC  Jown        Snow      Boss           1200010   26092009ACTIVE    " // Sample payload
}
```
```
{
    "id": "ABC",
    "responseId": "a008fe76-55ae-4aee-8a87-e6cfb81ff17a", //Randomic UUID
    "duration": 148 //Total Processing Spent time  
}
```
* **Consumer (kafka-consumer)**: The Producer instance, provided by Spring Boot Batch with a consumer connected to a Kafka Topic (topicInbound) to receive the request messages, sleep some time between the *durationMin* and *durationMax* parameters, generate a Randomic UUID and reply to other topic (topicOutbound)
* **Load Test (main folder)**: [load test]src/test/scala/producer/ProducerLoadTest.scala#L9) using [Gatling](https://gatling.io/), a very powerfull, flexible and light tool, with the desired scenarios and simultaneous users.

## Improving the solution

1. **Identify the producer**: The first thing to be done was identifying the producer by an unique Id between the other instances, and send that Id attached to the request message, and reply it again together with the response. And finally, simply filter the message (ex: [RecordFilterStrategy](blob/a47fae83052f37f3f18fc79eef6280b753236e30/kafka-producer/src/main/java/com/github/ricardocomar/kafkabalancedconsumers/kafkaproducer/config/KafkaConsumerConfig.java#L46) )

2. **Create unique consumer groups**: As mentioned before, the producers must be attached to a consumer group, so we set a unique group for each instance, on the @KafkaListener annotation (ex: 
[DiscardingResponseConsumer](blob/a47fae83052f37f3f18fc79eef6280b753236e30/kafka-producer/src/main/java/com/github/ricardocomar/kafkabalancedconsumers/kafkaproducer/consumer/DiscardingResponseConsumer.java#L34) )

## Testing the benefits

To confirm the gain of resilience and better response times, 
we emulated the original cenario by:
* Enabling the original producer's consummer group by adding a parameter "-Dspring.profiles.active=group" to be set on the producer start.
* Adding network delay on [ReleaseController](https://github.com/ricardo-comar/kafka-balanced-consumers/blob/a47fae83052f37f3f18fc79eef6280b753236e30/kafka-producer/src/main/java/com/github/ricardocomar/kafkabalancedconsumers/kafkaproducer/entrypoint/ReleaseController.java#L44), we simulate randomically a delay between 150-250ms, as the test on local machine don't have any latency.
* Generating a callback URL to go together on the request message.
* Adding a response consumer (also enabled by "group" profile) 
[GroupResponseConsumer](blob/a47fae83052f37f3f18fc79eef6280b753236e30/kafka-producer/src/main/java/com/github/ricardocomar/kafkabalancedconsumers/kafkaproducer/consumer/GroupResponseConsumer.java#L45) to handle the response, and if identified as the wrong consumer, redirects the response using the callback URL.

## Final Results

* **Analisys**: As observed on the results below, the *Unique Consumer Group* approach has a ~40% better performance, because the return message is consumed by the correct producer and replyed by the API, and we don't have  
* Statistics
  * Single Consumer  
![Statistics](img/stats-1.png)
  * Three Consumers  
![Statistics](img/stats-3.png)

* Response Time Distribution
  * Single Consumer  
![Statistics](img/rtd-1.png)
  * Three Consumers  
![Statistics](img/rtd-3.png)

* Response Time Percentiles over Time
  * Single Consumer  
![Statistics](img/rtp-1.png)
  * Three Consumers  
![Statistics](img/rtp-3.png)

* Requests Per Second
  * Single Consumer  
![Statistics](img/reqps-1.png)
  * Three Consumers  
![Statistics](img/reqps-3.png)

* Responses Per Second
  * Single Consumer  
![Statistics](img/resps-1.png)
  * Three Consumers  
![Statistics](img/resps-3.png)

# Let's Play !!

Now you will be able to run a local test :smile:

## Setting up the outside resources

### Kafka cluster and topics creation

First, start the resources with docker-compose:
```
docker-compose up
```

You should see a lot of messages, related to the first time instances creation and running. 

At end they should be all running. You can list the services using "docker ps -a" and test the services by trying to connect with telnet to each port (3306, 12181, 22181, 32181, 19092, 29092, 39092).

```
CONTAINER ID        IMAGE                              COMMAND                  CREATED             STATUS                    PORTS                                                    NAMES
bf149d33b65d        confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              9092/tcp, 0.0.0.0:19092->19092/tcp                       kafka-balanced-consumers_kafka-1_1
bd175d847b55        confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              9092/tcp, 0.0.0.0:29092->29092/tcp                       kafka-balanced-consumers_kafka-2_1
76229db017ba        confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              9092/tcp, 0.0.0.0:39092->39092/tcp                       kafka-balanced-consumers_kafka-3_1
76da9a64dd03        confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              2181/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:12181->12181/tcp   kafka-balanced-consumers_zookeeper-1_1
8e3cba64ccf1        confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              2181/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:22181->22181/tcp   kafka-balanced-consumers_zookeeper-2_1
87c1acbb1472        confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   17 hours ago        Up 2 minutes              2181/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:32181->32181/tcp   kafka-balanced-consumers_zookeeper-3_1
fb95dd92017d        mysql:latest                       "docker-entrypoint.s…"   17 hours ago        Up 2 minutes              0.0.0.0:3306->3306/tcp, 33060/tcp                        kafka-balanced-consumers_db_1

```
Now we need to identify the zookeeper leader instance. Connect with telnet to the first zookeeper instance (port 12181) and send the command "stats". If it's not the leader, it will reply with "Mode: follower".  Try the next port (22181 or 31181) until you receive a "Mode: leader".

In my example I'll pick the first instance (port 12181) as the leader, and will connect to it with "docker exec -it 76da9a64dd03 /bin/bash", to setup the topics. Then, send this command to list the available topics: 

```
kafka-topics --zookeeper zookeeper-1:12181 --list
```
Now you should see only two topics, used by kafka to keep things running. You can check their information by asking to describe them:
```
kafka-topics --zookeeper zookeeper-1:12181 --describe 
```
Now, let's create our topics. Run these two lines:
```
kafka-topics --zookeeper zookeeper-1:12181 --create --topic topicOutbound --partitions 6 --replication-factor 3
kafka-topics --zookeeper zookeeper-1:12181 --create --topic topicInbound --partitions 6 --replication-factor 3
```
And now check if they are created as expected:
```
kafka-topics --zookeeper zookeeper-1:12181 --describe
```
Obs: If, for some reason, you want to recreate the topics, first you need to delete them:
```
kafka-topics --zookeeper zookeeper-1:12181 --delete --topic topicInbound
kafka-topics --zookeeper zookeeper-1:12181 --delete --topic topicOutbound
```

## Running all together

![](img/terminal.png)


### Starting Kafka cluster 

As described above, run "docker-compose up" on main folder.


### Starting Load Balancer

On folder "kafka-border-service", start the service with "mvn spring-boot:run"
You can check in your brownser if it's running by openning 
* http://localhost:8080/


### Starting Producers

On folder "kafka-producer", start the service with "mvn spring-boot:run". It's recommended to start more than one instance of it. If you wish to run with same consumer group strategy, just add "-Dspring.profiles.active=group" to the end.

After a few seconds, you can check if it's registred on Eureka by opening in your browser http://localhost:8080/eureka/apps

You can also check if they are available on load balancer (at least one of them) by opening http://localhost:8080/app/producer/actuator/health


### Starting Consumers

On folder "kafka-consumer", start the service with "mvn spring-boot:run". It's recomended to start more than one instance of it. 


### Monitoring the topics

On another terminals, I recomend to monitor the topics with "kafkacat", a simple but powerful command line interface. Just run these two command, on separated windows:
```
kafkacat -C -b kafka-1:29092 -t topicInbound
kafkacat -C -b kafka-1:29092 -t topicOutbound 
```

### Final check

On your browser open a tab to Swagger interface:
* http://localhost:8080/app/producer/swagger-ui.html

### Running a Performance Test

Now, in another terminal, on main folder you can start the load tests by running "mvn gatling:run".

You will be able to monitor the progress, with a few informations about the success of the requests (or failures).

## References

* https://better-coding.com/building-apache-kafka-cluster-using-docker-compose-and-virtualbox/
* https://spring.io/guides/gs/rest-service/
* https://github.com/edenhill/kafkacat
* https://www.baeldung.com/spring-kafka
* https://docs.spring.io/spring-kafka/reference/html/#kafka
* https://dzone.com/articles/20-best-practices-for-working-with-apache-kafka-at
* https://www.baeldung.com/java-concurrent-locks
* https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html
* https://www.baeldung.com/spring-cloud-netflix-eureka
* https://www.baeldung.com/zuul-load-balancing
* https://www.treinaweb.com.br/blog/documentando-uma-api-spring-boot-com-o-swagger/
* http://dev.splunk.com/view/splunk-logging-java/SP-CAAAE3R
* https://github.com/simplesteph/kafka-stack-docker-compose
* https://www.confluent.io/blog/schema-registry-avro-in-spring-boot-application-tutorial
* https://msayag.github.io/Kafka/
* https://memorynotfound.com/spring-kafka-adding-custom-header-kafka-message-example/
* https://developer.ibm.com/tutorials/mq-jms-application-development-with-spring-boot/
