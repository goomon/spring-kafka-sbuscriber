package dev.iitp.subscriber.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SensorRecord {

    private String userId;
    private long timestamp;
    private Value value;

    @ToString
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Value {
        private Accelerometer acc;
        private BloodVolumePressure bvp;
        private ElectrodernalActivity eda;
        private HeartRate hr;
        private Temperature temp;
    }
}
