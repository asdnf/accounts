package org.ppk.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.ppk.accounts.dto.SyncLease;

import java.util.Map;

public class LeaseDeserializer implements Deserializer<SyncLease> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SyncLease deserialize(String topic, byte[] data) {
        String strData = new String(data, 0, data.length);
        try {
            return mapper.readValue(strData, SyncLease.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to deserialize a lease", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public SyncLease deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public void close() {

    }
}
