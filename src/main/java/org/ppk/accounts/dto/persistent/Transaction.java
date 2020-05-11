package org.ppk.accounts.dto.persistent;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    // I'm not a good specialist about money calculation. So I leave this type as Long.
    // Possibly we should think about type memory size and proper rounding when picking a type.
    private Long amount;

    @ManyToOne
    private Account destination;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean processed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Account getDestination() {
        return destination;
    }

    public void setDestination(Account destination) {
        this.destination = destination;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", destination=" + destination +
                ", processed=" + processed +
                '}';
    }
}
