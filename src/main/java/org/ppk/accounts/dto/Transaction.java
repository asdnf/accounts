package org.ppk.accounts.dto;

import java.math.BigDecimal;

public class Transaction {

    private String id;
    private BigDecimal amount;
    private String srcId;
    private String dstId;

    public Transaction() {
    }

    public Transaction(String id, String amount, String srcId, String dstId) {
        this.id = id;
        this.amount = new BigDecimal(amount);
        this.srcId = srcId;
        this.dstId = dstId;
    }

    public Transaction(String id, BigDecimal amount, String srcId, String dstId) {
        this.id = id;
        this.amount = amount;
        this.srcId = srcId;
        this.dstId = dstId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getDstId() {
        return dstId;
    }

    public void setDstId(String dstId) {
        this.dstId = dstId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", srcId='" + srcId + '\'' +
                ", dstId='" + dstId + '\'' +
                '}';
    }
}
