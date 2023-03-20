package dev.iitp.subscriber.transformer;

import dev.iitp.subscriber.model.SensorRecord;
import dev.iitp.subscriber.model.cache.SensorWindowQueue;
import dev.iitp.subscriber.model.feature.SensorRecordFeature;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.LinkedList;

public class SensorWindowTransformer implements ValueTransformer<SensorRecord, SensorRecordFeature> {

    private KeyValueStore<String, SensorWindowQueue> stateStore;
    private String storeName;
    private double samplingRate;

    public SensorWindowTransformer(String storeName, long windowSize, double overlapRatio) {
        this.storeName = storeName;
        this.samplingRate = (double) windowSize * (1 - overlapRatio);
    }

    @Override
    public void init(ProcessorContext context) {
        stateStore = context.getStateStore(storeName);
    }

    @Override
    public SensorRecordFeature transform(SensorRecord value) {
        String userId = value.getUserId();
        long timestamp = value.getTimestamp();
        SensorWindowQueue sensorWindowQueue = stateStore.get(userId);
        if (sensorWindowQueue == null || sensorWindowQueue.getSensorRecordQueue() == null) {
            stateStore.put(userId, new SensorWindowQueue(timestamp, timestamp, new LinkedList<>()));
            sensorWindowQueue = stateStore.get(userId);
        }
        sensorWindowQueue.setTimestamp(timestamp);
        sensorWindowQueue.push(value);
        if (sensorWindowQueue.getQueueSize() > 3) {
            sensorWindowQueue.pop();
        }

        // According to sampling rate, extract features.
        if (timestamp - sensorWindowQueue.getLastExtracted() < samplingRate) {
            return null;
        } else {
            sensorWindowQueue.setLastExtracted(timestamp);
            stateStore.put(userId, sensorWindowQueue);
            return sensorWindowQueue.extractFeature(userId);
        }
    }

    @Override
    public void close() {

    }
}
