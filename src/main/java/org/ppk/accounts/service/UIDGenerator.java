package org.ppk.accounts.service;

import java.util.UUID;

public class UIDGenerator {

    public synchronized String getUID() {
        return UUID.randomUUID().toString();
    }

}
