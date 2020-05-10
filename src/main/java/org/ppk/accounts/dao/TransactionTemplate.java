package org.ppk.accounts.dao;

import org.ppk.accounts.dto.persistent.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface TransactionTemplate extends JpaRepository<Transaction, Integer> {

    Stream<Transaction> findByProcessedFalse();

}
