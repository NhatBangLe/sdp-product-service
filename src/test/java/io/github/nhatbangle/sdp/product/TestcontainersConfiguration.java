package io.github.nhatbangle.sdp.product;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

//    @Bean
//    @ServiceConnection
//    KafkaContainer kafkaContainer() {
//        return new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.9.0"));
//    }
//
//    @Bean
//    @ServiceConnection(versionName = "redis")
//    GenericContainer<?> redisContainer() {
//        return new GenericContainer<>(DockerImageName.parse("redis:7.4.0-v1")).withExposedPorts(6379);
//    }

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:8.1"))
                .withDatabaseName("product_service");
    }

}
