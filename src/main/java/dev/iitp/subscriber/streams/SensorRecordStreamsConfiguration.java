package dev.iitp.subscriber.streams;

import dev.iitp.subscriber.model.SensorRecord;
import dev.iitp.subscriber.model.cache.SensorWindowQueue;
import dev.iitp.subscriber.model.feature.SensorRecordFeature;
import dev.iitp.subscriber.repository.SensorFeatureRepository;
import dev.iitp.subscriber.transformer.SensorWindowTransformer;
import dev.iitp.subscriber.util.extractor.SensorRecordTimestampExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.transaction.Transactional;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class SensorRecordStreamsConfiguration {

    private static String APPLICATION_ID = "sensor-data-streams";

    @Value("${user.topic}")
    private String topic;
    @Value("${user.preprocess.window.size}")
    private long windowSize;
    @Value("${user.preprocess.window.overlap.ratio}")
    private double overlapRatio;

    private final SensorRecordTimestampExtractor timestampExtractor;
    private final KafkaProperties kafkaProperties;
    private final SensorFeatureRepository repository;

    @Bean
    @Transactional
    public KStream<String, SensorRecordFeature> kStream(StreamsBuilder streamsBuilder) {
        // Serde settings.
        Serde<String> stringSerde = Serdes.String();
        JsonSerde<SensorRecord> sensorRecordJsonSerde = new JsonSerde(new JsonSerializer(), new JsonDeserializer(SensorRecord.class, false));
        JsonSerde<SensorWindowQueue> sensorWindowQueueJsonSerde = new JsonSerde<>(new JsonSerializer<>(), new JsonDeserializer<>(SensorWindowQueue.class, false));

        // StateStore settings.
        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore(APPLICATION_ID);
        StoreBuilder<KeyValueStore<String, SensorWindowQueue>> storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, sensorWindowQueueJsonSerde);
        streamsBuilder.addStateStore(storeBuilder);

        // Stream definition.
        KStream<String, SensorRecordFeature> stream = streamsBuilder.stream(topic, Consumed.with(stringSerde, sensorRecordJsonSerde)
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST)
                        .withTimestampExtractor(timestampExtractor))
                .transformValues(() -> new SensorWindowTransformer(APPLICATION_ID, windowSize, overlapRatio), APPLICATION_ID)
                .filter((key, value) -> value != null);

        // Save to Database.
        stream.foreach((key, value) ->  repository.save(value));
        stream.print(Printed.<String, SensorRecordFeature>toSysOut().withLabel("debug"));
        return stream;
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = kafkaProperties.buildStreamsProperties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sensor-data-streams");
        props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "sensor-data-streams");
        return new KafkaStreamsConfiguration(props);
    }
}
