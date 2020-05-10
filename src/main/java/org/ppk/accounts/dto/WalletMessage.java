package org.ppk.accounts.dto;

public class WalletMessage {

    private String id;
    private Transaction transaction;
    private String errorFound;

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

    public String getErrorFound() {
        return errorFound;
    }

    public void setErrorFound(String errorFound) {
        this.errorFound = errorFound;
    }

    @Override
    public String toString() {
        return "WalletMessage{" +
                "id='" + id + '\'' +
                ", transaction=" + transaction +
                ", errorFound='" + errorFound + '\'' +
                '}';
    }
}
