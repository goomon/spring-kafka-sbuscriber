package dev.iitp.subscriber.model.feature;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter @Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SensorRecordFeature {

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private long timestamp;
    @Embedded
    private Value value;

    @ToString
    @Embeddable
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Value {
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "xMean", column = @Column(name = "acc_x_mean")),
                @AttributeOverride(name = "yMean", column = @Column(name = "acc_y_mean")),
                @AttributeOverride(name = "zMean", column = @Column(name = "acc_z_mean")),
                @AttributeOverride(name = "xStd", column = @Column(name = "acc_x_std")),
                @AttributeOverride(name = "yStd", column = @Column(name = "acc_y_std")),
                @AttributeOverride(name = "zStd", column = @Column(name = "acc_z_std")),
        })
        private AccelerometerFeature acc;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "mean", column = @Column(name = "bvp_mean")),
                @AttributeOverride(name = "std", column = @Column(name = "bvp_std")),
        })
        private BloodVolumePressureFeature bvp;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "mean", column = @Column(name = "eda_mean")),
                @AttributeOverride(name = "std", column = @Column(name = "eda_std")),
        })
        private ElectrodernalActivityFeature eda;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "mean", column = @Column(name = "hr_mean")),
                @AttributeOverride(name = "std", column = @Column(name = "hr_std")),
        })
        private HeartRateFeature hr;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "mean", column = @Column(name = "temp_mean")),
                @AttributeOverride(name = "std", column = @Column(name = "temp_std")),
        })
        private TemperatureFeature temp;
    }
}
