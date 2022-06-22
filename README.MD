# ATM Banking App With Kafka
The application based on Spring Boot and dockerised Kafka. 
Application API allow client to manage their balance. There are three ways.
**Deposit**, **Withdraw** and **Check** their account balance.

## Start application
Start Kafka with Zookeeper in Docker (you need install Docker first - [Installation Guide](https://docs.docker.com/get-started/)).
```
docker-compose up -d
```
Start spring app
```
mvn clear spring-boot:run
```