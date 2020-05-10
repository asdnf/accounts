package org.ppk.accounts;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.ppk.accounts.dto.SyncLease;
import org.ppk.accounts.dto.Transaction;
import org.ppk.accounts.service.LeaseDeserializer;
import org.ppk.accounts.service.LeaseSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SyncLeaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(SyncLeaseConfig.class);

    @Bean
    public KafkaTemplate<String, SyncLease> leaseTemplate() {
        return new KafkaTemplate(kafkaProducer());
    }

//    public ConsumerFactory<Integer, String> kafkaConsumer() {
//        return new DefaultKafkaConsumerFactory<>(consumerProps());
//    }


    public ProducerFactory<String, Transaction> kafkaProducer() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

//    public Map<String, Object> consumerProps() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "95.213.195.157:9092");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LeaseDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
////        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
////        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
////        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
//        return props;
//    }

    public Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "95.213.195.157:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LeaseSerializer.class);
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        return props;
    }


}
