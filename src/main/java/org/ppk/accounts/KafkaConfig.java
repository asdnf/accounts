package org.ppk.accounts;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.ppk.accounts.dto.SyncLease;
import org.ppk.accounts.dto.WalletMessage;
import org.ppk.accounts.dto.persistent.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

//    @Value("${kafka.bootstrap.host}")
//    private String kafkaHost;
//    @Value("${kafka.bootstrap.port}")
//    private String kafkaPort;

//    @Value("${spring.kafka.bootstrap.servers}")
//    private String kafkaBootstrap;


    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(), jsonDeserializer);
    }

//    @Bean
//    public KafkaAdmin admin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap);
//        return new KafkaAdmin(configs);
//    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transaction")
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic walletTopic() {
        return TopicBuilder.name("wallet")
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic leaseTopic() {
        return TopicBuilder.name("lease")
                .replicas(1)
                .compact()
                .build();
    }

}
