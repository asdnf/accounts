package org.ppk.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.ppk.accounts.dto.WalletMessage;

import java.util.Map;

public class WalletSerializer implements Serializer<WalletMessage> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, WalletMessage data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize a WalletMessage", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Headers headers, WalletMessage data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {

    }
}
