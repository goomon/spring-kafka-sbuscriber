package dev.iitp.subscriber.model.feature;

import lombok.*;

import javax.persistence.Embeddable;

@ToString
@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BloodVolumePressureFeature {

    private double mean;
    private double std;
}
