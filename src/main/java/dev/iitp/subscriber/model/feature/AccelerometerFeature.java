package dev.iitp.subscriber.model.feature;

import lombok.*;

import javax.persistence.Embeddable;

@ToString
@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccelerometerFeature {

    private double xMean;
    private double yMean;
    private double zMean;

    private double xStd;
    private double yStd;
    private double zStd;
}
