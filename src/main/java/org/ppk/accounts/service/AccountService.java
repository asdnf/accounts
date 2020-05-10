package org.ppk.accounts.service;

import org.ppk.accounts.dao.AccountTemplate;
import org.ppk.accounts.dto.persistent.Account;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public class AccountService {

    @Autowired
    private AccountTemplate accountTemplate;

    @Autowired
    private AccountFactory accountFactory;

    @Transactional
    public void generateTenAccounts() {
        for (int i = 0; i < 10; i++) {
            accountTemplate.save(accountFactory.createAccount(42l, 0l));
        }
    }

    @Transactional
    public Account getAccountById(String id) {
        return getAccountById(Integer.valueOf(id));
    }

    @Transactional
    public Account getAccountById(Integer id) {
        return accountTemplate.getOne(id);
    }

}
