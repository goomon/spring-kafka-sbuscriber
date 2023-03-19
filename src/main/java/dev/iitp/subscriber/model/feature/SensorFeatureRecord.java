package dev.iitp.subscriber.model.feature;

import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorFeatureRecord {

    private String userId;
    private long timestamp;
    private Value value;

    @ToString
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Value {
        private AccelerometerFeature acc;
        private BloodVolumePressureFeature bvp;
        private ElectrodernalActivityFeature eda;
        private HeartRateFeature hr;
        private TemperatureFeature temp;
    }
}
