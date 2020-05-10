package org.ppk.accounts.service;

import org.junit.jupiter.api.Test;

class TransactionProcessorServiceTest {

    @Test
    void assertIntegerOverflow() {
        long halfOfMaxValue = Long.MAX_VALUE / 2;
        long closeToTopValue = halfOfMaxValue + halfOfMaxValue / 2;

        assert !new TransactionProcessorService().assertIntegerOverflow(closeToTopValue, closeToTopValue);
        assert !new TransactionProcessorService().assertIntegerOverflow(Long.MAX_VALUE, Long.MAX_VALUE);
        assert !new TransactionProcessorService().assertIntegerOverflow(Long.MAX_VALUE - 1, Long.MAX_VALUE - 1);

        assert new TransactionProcessorService().assertIntegerOverflow(0, Long.MIN_VALUE);
        assert new TransactionProcessorService().assertIntegerOverflow(0, Long.MAX_VALUE);
        assert new TransactionProcessorService().assertIntegerOverflow(0, Long.MIN_VALUE + 1);
        assert new TransactionProcessorService().assertIntegerOverflow(0, Long.MAX_VALUE - 1);

        assert new TransactionProcessorService().assertIntegerOverflow(halfOfMaxValue, halfOfMaxValue);
        assert !new TransactionProcessorService().assertIntegerOverflow(halfOfMaxValue, halfOfMaxValue + 2);
    }
}