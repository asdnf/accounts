package org.ppk.accounts.dao;

import org.ppk.accounts.dto.persistent.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
