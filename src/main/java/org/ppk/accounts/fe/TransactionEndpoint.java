package org.ppk.accounts.fe;

import org.ppk.accounts.dto.persistent.Account;
import org.ppk.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account")
public class TransactionEndpoint {

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Account getAccountForId(@PathVariable("id") String id) {
        Account account = accountService.getAccountById(id);
        return account;
    }

    @RequestMapping(path = "/fill", method = RequestMethod.GET)
    @ResponseBody
    public void fillAccounts() {
        accountService.generateTenAccounts();
    }

}
