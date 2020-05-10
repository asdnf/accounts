package org.ppk.accounts;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.ppk.accounts.dto.SyncLease;
import org.ppk.accounts.dto.Transaction;
import org.ppk.accounts.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


@Configuration
@EnableKafka
@EnableAsync
@EnableScheduling
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public TransactionProcessorService transactionProcessorService() {
        return new TransactionProcessorService();
    }

    @Bean
    public ServiceIdentifier serviceIdentifier() {
        return new ServiceIdentifier();
    }

    @Bean
    public CurrentDateGenerator currentDateGenerator() {
        return new CurrentDateGenerator();
    }

    @Bean
    public UIDGenerator uidGenerator() {
        return new UIDGenerator();
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "95.213.195.157:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transaction")
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic walletTopic() {
        return TopicBuilder.name("wallet")
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory(
            ConsumerFactory<Integer, String> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(kafkaConsumerFactory());
        factory.setConcurrency(5);
        return factory;
    }

    @Bean
    public SyncLeaseFactory syncLeaseFactory() {
        return new SyncLeaseFactory();
    }

    @Bean
    public AccountFactory accountFactory() {
        return new AccountFactory();
    }

    @Bean
    public AccountService accountService() {
        return new AccountService();
    }

    @Bean
    public KafkaTemplate<String, Transaction> transactionTemplate(
            ProducerFactory<String, Transaction> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public KafkaTemplate<String, SyncLease> leaseTemplate(
            ProducerFactory<String, SyncLease> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public ConsumerFactory<Integer, String> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    @Bean
    public ProducerFactory<String, Transaction> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "95.213.195.157:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TransactionDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        return props;
    }

    public Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "95.213.195.157:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionSerializer.class);
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        return props;
    }

}
