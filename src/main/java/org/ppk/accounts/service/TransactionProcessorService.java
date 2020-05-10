package org.ppk.accounts.service;

import org.ppk.accounts.dao.TransactionTemplate;
import org.ppk.accounts.dto.WalletMessage;
import org.ppk.accounts.dto.persistent.Account;
import org.ppk.accounts.dto.persistent.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import javax.transaction.Transactional;

public class TransactionProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessorService.class);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private KafkaTemplate<String, WalletMessage> walletTemplate;

    @Autowired
    private UIDGenerator uidGenerator;

    @Autowired
    private WalletMessageFactory walletMessageFactory;

    @Transactional
    public void processTransactionsAsLeader() {
        transactionTemplate.findByProcessedFalse().forEach(tr -> processSingleDBTransaction(tr));
    }

    public boolean assertAccountValueNonNegative(long a) {
        return a >= 0;
    }

    public boolean assertIntegerOverflow(long a, long b) {
        if (b < 0) {
            return a <= Long.MIN_VALUE + b;
        }
        return a <= Long.MAX_VALUE - b;
    }

    public boolean assertResultNonNegative(long a, long b) {
        return a - b >= 0;
    }

    public void processSingleDBTransaction(Transaction transaction) {
        Account dest = transaction.getDestination();
        long amount = transaction.getAmount();
        long actualValue = dest.getAmount();

        boolean valid = assertAccountValueNonNegative(actualValue) &&
                assertIntegerOverflow(actualValue, amount) &&
                assertResultNonNegative(actualValue, amount);

        if (!assertAccountValueNonNegative(actualValue)) {
            logger.error("Error while processing transaction: account value appears to be negative");
            return;
        }

        if (!assertIntegerOverflow(actualValue, amount)) {
            logger.error("Error while processing transaction: sum of account value and " +
                    "incremental value overflows memory, allocated for account data");
            return;
        }

        if (!assertResultNonNegative(actualValue, amount)) {
            logger.error("Error while processing transaction: sum of account value and " +
                    "incremental value results with negative value");
            return;
        }

        long result = actualValue + amount;
        transaction.setProcessed(true);
        transaction.setAmount(result);

        transactionTemplate.saveAndFlush(transaction);

        String uid = uidGenerator.getUID();
        WalletMessage walletMessage = walletMessageFactory.createWalletMessage();
        walletMessage.setId(uid);
        walletMessage.setTransaction(transaction);
        walletTemplate.send(uid, walletMessage);
    }


}
