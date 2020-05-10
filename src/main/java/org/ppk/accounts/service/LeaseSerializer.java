package org.ppk.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.ppk.accounts.dto.SyncLease;

import java.util.Map;

public class LeaseSerializer implements Serializer<SyncLease> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, SyncLease data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize a lease", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Headers headers, SyncLease data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {

    }
}
