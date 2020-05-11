package org.ppk.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.ppk.accounts.dto.persistent.Transaction;

import java.util.Map;

public class TransactionSerializer implements Serializer<Transaction> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Transaction data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize a transaction", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Headers headers, Transaction data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {

    }
}
