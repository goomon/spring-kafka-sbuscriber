package dev.iitp.subscriber.util.extractor;

import dev.iitp.subscriber.model.SensorRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.stereotype.Component;

@Component
public class SensorRecordTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        SensorRecord sensorRecord = (SensorRecord) record.value();
        return sensorRecord.getTimestamp();
    }
}
