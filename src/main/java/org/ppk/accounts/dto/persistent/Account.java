package org.ppk.accounts.dto.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private Long owner;
    // I'm not a good specialist about money calculation. So I leave this type as Long.
    // Possibly we should think about type memory size and proper rounding when picking a type.
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
