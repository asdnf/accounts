package org.ppk.accounts.service;

import java.util.UUID;

public class ServiceIdentifier {

    private final String identifier;

    public ServiceIdentifier() {
        this.identifier = UUID.randomUUID().toString();
    }

    public String getIdentifier() {
        return identifier;
    }
}
