package dev.iitp.subscriber.model.cache;

import dev.iitp.subscriber.model.SensorRecord;
import lombok.AllArgsConstructor;

import java.util.Queue;

@AllArgsConstructor
public class SensorWindowQueue {

    private long timestamp;
    private Queue<SensorRecord> sensorMeanRecordQueue;

    public void push(SensorRecord record) {
        sensorMeanRecordQueue.add(record);
    }

    public void pop() {
        sensorMeanRecordQueue.poll();
    }

    public int getQueueSize() {
        return sensorMeanRecordQueue.size();
    }
}
