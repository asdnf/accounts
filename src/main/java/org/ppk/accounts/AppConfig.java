package org.ppk.accounts;

import org.ppk.accounts.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public LeaderRoleService leaderRoleService() {
        return new LeaderRoleService();
    }

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
    public WalletMessageFactory walletMessageFactory() {
        return new WalletMessageFactory();
    }

}
