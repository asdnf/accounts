package org.ppk.accounts.service;

import org.ppk.accounts.dao.TransactionTemplate;
import org.ppk.accounts.dto.WalletMessage;
import org.ppk.accounts.dto.persistent.Account;
import org.ppk.accounts.dto.persistent.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import javax.transaction.Transactional;

public class TransactionProcessorService {

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
            String uid = uidGenerator.getUID();
            WalletMessage walletMessage =
                    walletTemplate.send(uidGenerator.getUID(), )
        }
    }


}
