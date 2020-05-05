package org.ppk.accounts.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


@Configuration
public class AppConfig {

    public static Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private Random rnd = new Random();

    @Autowired
    private KafkaTemplate<String, String> template;

    @KafkaListener(topics = "transaction")
    public void listen(ConsumerRecord<String, Transaction> cr) {
        logger.info(cr.toString());
        logger.info(cr.value().toString());

        ObjectMapper mapper = new ObjectMapper();

        Transaction transaction = new Transaction(
                "transaction name: " + UUID.randomUUID().toString(), rnd.nextInt());

        try {
            template.send("wallet", mapper.writeValueAsString(transaction));
        } catch (JsonProcessingException e) {
            logger.error("wallet write failed", e);
        }
    }

    @Bean
    public DefaultKafkaConsumerFactory<Integer, String> kafkaConsumerFactory(Map<String, Object> consumerProps) {
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

//    @Bean
//    public KafkaMessageListenerContainer<Integer, String> createContainer(
//            Map<String, Object> consumerProps,
//            ContainerProperties containerProps) {
//        return new KafkaMessageListenerContainer<>(consumerProps, containerProps);
//    }

    @Bean
    public ProducerFactory<String, Transaction> kafkaProducerFactory(Map<String, Object> senderProps) {
        return new DefaultKafkaProducerFactory<>(senderProps);
    }

    @Bean
    public KafkaTemplate<String, Transaction> template(
            ProducerFactory<String, Transaction> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka.service:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka.service:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

}
