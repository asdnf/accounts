package org.ppk.accounts.dto;

import org.ppk.accounts.dto.persistent.Transaction;

public class WalletMessage {

    private String id;
    private Transaction transaction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "WalletMessage{" +
                "id='" + id + '\'' +
                ", transaction=" + transaction +
                '}';
    }
}
