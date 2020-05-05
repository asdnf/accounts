package org.ppk.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.ppk.accounts.dto.Transaction;

import java.util.Map;

public class TransactionDeserializer implements Deserializer<Transaction> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Transaction deserialize(String topic, byte[] data) {
        String strData = new String(data, 0, data.length);
        try {
            return mapper.readValue(strData, Transaction.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to deserialize a transaction", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Transaction deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public void close() {

    }
}
