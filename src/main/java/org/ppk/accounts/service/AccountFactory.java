package org.ppk.accounts.service;

import org.ppk.accounts.dto.persistent.Account;

public class AccountFactory {

    public Account createAccount() {
        return new Account();
    }

    public Account createAccount(Long owner, Long amount) {
        Account account = createAccount();
        account.setOwner(owner);
        account.setAmount(amount);
        return account;
    }

}
